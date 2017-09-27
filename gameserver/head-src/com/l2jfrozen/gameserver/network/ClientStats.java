/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jfrozen.gameserver.network;

import com.l2jfrozen.netcore.Config;

public class ClientStats
{
	public int processedPackets = 0;
	public int droppedPackets = 0;
	public int unknownPackets = 0;
	public int totalQueueSize = 0;
	public int maxQueueSize = 0;
	public int totalBursts = 0;
	public int maxBurstSize = 0;
	public int shortFloods = 0;
	public int longFloods = 0;
	public int totalQueueOverflows = 0;
	public int totalUnderflowExceptions = 0;
	
	private final int[] _packetsInSecond;
	private long _packetCountStartTick = 0;
	private int _head;
	private int _totalCount = 0;
	
	private int _floodsInMin = 0;
	private long _floodStartTick = 0;
	private int _unknownPacketsInMin = 0;
	private long _unknownPacketStartTick = 0;
	private int _overflowsInMin = 0;
	private long _overflowStartTick = 0;
	private int _underflowReadsInMin = 0;
	private long _underflowReadStartTick = 0;
	
	private volatile boolean _floodDetected = false;
	private volatile boolean _queueOverflowDetected = false;
	
	private final int BUFFER_SIZE;
	
	public ClientStats()
	{
		BUFFER_SIZE = Config.getInstance().CLIENT_PACKET_QUEUE_MEASURE_INTERVAL;
		_packetsInSecond = new int[BUFFER_SIZE];
		_head = BUFFER_SIZE - 1;
	}
	
	/**
	 * @return true if incoming packet need to be dropped
	 */
	protected final boolean dropPacket()
	{
		final boolean result = _floodDetected || _queueOverflowDetected;
		if (result)
			droppedPackets++;
		return result;
	}
	
	/**
	 * Later during flood returns true (and send ActionFailed) once per second.
	 * @param queueSize 
	 * @return true if flood detected first and ActionFailed packet need to be sent.
	 */
	protected final boolean countPacket(int queueSize)
	{
		processedPackets++;
		totalQueueSize += queueSize;
		if (maxQueueSize < queueSize)
			maxQueueSize = queueSize;
		if (_queueOverflowDetected && queueSize < 2)
			_queueOverflowDetected = false;
		
		return countPacket();
	}
	
	/**
	 * Counts unknown packets and return true if threshold is reached.
	 * @return 
	 */
	protected final boolean countUnknownPacket()
	{
		unknownPackets++;
		
		final long tick = System.currentTimeMillis();
		if (tick - _unknownPacketStartTick > 60000)
		{
			_unknownPacketStartTick = tick;
			_unknownPacketsInMin = 1;
			return false;
		}
		
		_unknownPacketsInMin++;
		return _unknownPacketsInMin > Config.getInstance().CLIENT_PACKET_QUEUE_MAX_UNKNOWN_PER_MIN;
	}
	
	/**
	 * Counts burst length and return true if execution of the queue need to be aborted.
	 * @param count - current number of processed packets in burst
	 * @return 
	 */
	protected final boolean countBurst(int count)
	{
		if (count > maxBurstSize)
			maxBurstSize = count;
		
		if (count < Config.getInstance().CLIENT_PACKET_QUEUE_MAX_BURST_SIZE)
			return false;
		
		totalBursts++;
		return true;
	}
	
	/**
	 * Counts queue overflows and return true if threshold is reached.
	 * @return 
	 */
	protected final boolean countQueueOverflow()
	{
		_queueOverflowDetected = true;
		totalQueueOverflows++;
		
		final long tick = System.currentTimeMillis();
		if (tick - _overflowStartTick > 60000)
		{
			_overflowStartTick = tick;
			_overflowsInMin = 1;
			return false;
		}
		
		_overflowsInMin++;
		return _overflowsInMin > Config.getInstance().CLIENT_PACKET_QUEUE_MAX_OVERFLOWS_PER_MIN;
	}
	
	/**
	 * Counts underflow exceptions and return true if threshold is reached.
	 * @return 
	 */
	protected final boolean countUnderflowException()
	{
		totalUnderflowExceptions++;
		
		final long tick = System.currentTimeMillis();
		if (tick - _underflowReadStartTick > 60000)
		{
			_underflowReadStartTick = tick;
			_underflowReadsInMin = 1;
			return false;
		}
		
		_underflowReadsInMin++;
		return _underflowReadsInMin > Config.getInstance().CLIENT_PACKET_QUEUE_MAX_UNDERFLOWS_PER_MIN;
	}
	
	/**
	 * Returns true if maximum number of floods per minute is reached.
	 * @return 
	 */
	protected final boolean countFloods()
	{
		return _floodsInMin > Config.getInstance().CLIENT_PACKET_QUEUE_MAX_FLOODS_PER_MIN;
	}
	
	private final boolean longFloodDetected()
	{
		return (_totalCount / BUFFER_SIZE) > Config.getInstance().CLIENT_PACKET_QUEUE_MAX_AVERAGE_PACKETS_PER_SECOND;
	}
	
	/**
	 * Returns true if flood detected first and ActionFailed packet need to be sent.
	 * Later during flood returns true (and send ActionFailed) once per second.
	 * @return 
	 */
	private final synchronized boolean countPacket()
	{
		_totalCount++;
		final long tick = System.currentTimeMillis();
		if (tick - _packetCountStartTick > 1000)
		{
			_packetCountStartTick = tick;
			
			// clear flag if no more flooding during last seconds
			if (_floodDetected
					&& !longFloodDetected()
					&& _packetsInSecond[_head] < Config.getInstance().CLIENT_PACKET_QUEUE_MAX_PACKETS_PER_SECOND / 2)
				_floodDetected = false;
			
			// wrap head of the buffer around the tail
			if (_head <= 0)
				_head = BUFFER_SIZE;
			_head--;
			
			_totalCount -= _packetsInSecond[_head];
			_packetsInSecond[_head] = 1;
			return _floodDetected;
		}
		
		final int count = ++_packetsInSecond[_head];
		if (!_floodDetected)
		{
			if (count > Config.getInstance().CLIENT_PACKET_QUEUE_MAX_PACKETS_PER_SECOND)
				shortFloods++;
			else if (longFloodDetected())
				longFloods++;
			else
				return false;
			
			_floodDetected = true;
			if (tick - _floodStartTick > 60000)
			{
				_floodStartTick = tick;
				_floodsInMin = 1;
			}
			else
				_floodsInMin++;
			
			return true; // Return true only in the beginning of the flood
		}
		
		return false;
	}
}