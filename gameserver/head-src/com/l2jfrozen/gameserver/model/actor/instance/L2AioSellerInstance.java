/* This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jfrozen.gameserver.model.actor.instance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;

import javolution.text.TextBuilder;

import com.l2jfrozen.Config;
import com.l2jfrozen.gameserver.ai.CtrlIntention;
import com.l2jfrozen.gameserver.network.serverpackets.ActionFailed;
import com.l2jfrozen.gameserver.network.serverpackets.EtcStatusUpdate;
import com.l2jfrozen.gameserver.network.serverpackets.MyTargetSelected;
import com.l2jfrozen.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jfrozen.gameserver.network.serverpackets.ValidateLocation;
import com.l2jfrozen.gameserver.templates.L2NpcTemplate;
import com.l2jfrozen.util.CloseUtil;
import com.l2jfrozen.util.database.L2DatabaseFactory;

/**
 * @author RedHoT
 */
public class L2AioSellerInstance extends L2FolkInstance
{
	public L2AioSellerInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onAction(L2PcInstance player)
	{
		if (!canTarget(player))
			return;

		player.setLastFolkNPC(this);

		// Check if the L2PcInstance already target the L2NpcInstance
		if (this != player.getTarget())
		{
			// Set the target of the L2PcInstance player
			player.setTarget(this);

			// Send a Server->Client packet MyTargetSelected to the L2PcInstance player
			MyTargetSelected my = new MyTargetSelected(getObjectId(), 0);
			player.sendPacket(my);
			my = null;

			// Send a Server->Client packet ValidateLocation to correct the L2NpcInstance position and heading on the client
			player.sendPacket(new ValidateLocation(this));
		}
		else
		{
			// Calculate the distance between the L2PcInstance and the L2NpcInstance
			if (!canInteract(player))
			{
				// Notify the L2PcInstance AI with AI_INTENTION_INTERACT
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, this);
			}
			else
			{
				showHtmlWindow(player);
			}
		}
		// Send a Server->Client ActionFailed to the L2PcInstance in order to avoid that the client wait another packet
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}

	@Override
	public void onBypassFeedback(L2PcInstance player, String command)
	{
		int itemId = Config.AIO_ITEM_ID;
		int itemCount = Config.AIO_ITEM_CNT;

		L2ItemInstance itemInstance = player.getInventory().getItemByItemId(itemId);

		if (command.startsWith("add_aio_1"))
		{
			if (itemInstance == null || !itemInstance.isStackable() && player.getInventory().getInventoryItemCount(itemId, -1) < itemCount)
			{
				player.sendMessage("You do not have enough items!");
			return;
			}
			if (itemInstance.isStackable())
			{
				if (!player.destroyItemByItemId("Aio Seller", itemId, itemCount, player.getTarget(), true))
				{
					player.sendMessage("You do not have enough items!");
					return;
				}
			}
			else
				for (int i = 0; i < itemCount; i++)
					player.destroyItemByItemId("Aio Seller", itemId, 1, player.getTarget(), true);

			doAio(player, 30);
		}
		else if (command.startsWith("add_aio_2"))
		{
			if (itemInstance == null || !itemInstance.isStackable() && player.getInventory().getInventoryItemCount(itemId, -1) < (itemCount * 1.67))
			{
				player.sendMessage("You do not have enough items!");
				return;
			}
			if (itemInstance.isStackable())
			{
				if (!player.destroyItemByItemId("Aio Seller", itemId, (int) (itemCount * 1.67), player.getTarget(), true))
				{
					player.sendMessage("You do not have enough items!");
					return;
				}
			}
			else
				for (int i = 0; i < (itemCount * 1.67); i++)
					player.destroyItemByItemId("Aio Seller", itemId, 1, player.getTarget(), true);

			doAio(player, 60);
		}
		else if (command.startsWith("add_aio_3"))
		{
			if (itemInstance == null || !itemInstance.isStackable() && player.getInventory().getInventoryItemCount(itemId, -1) < (itemCount * 2.5))
			{
				player.sendMessage("You do not have enough items!");
				return;
			}
			if (itemInstance.isStackable())
			{
				if (!player.destroyItemByItemId("Aio Seller", itemId, (int) (itemCount * 2.5), player.getTarget(), true))
				{
					player.sendMessage("You do not have enough items!");
					return;
				}
			}
			else
				for (int i = 0; i < (itemCount * 2.5); i++)
					player.destroyItemByItemId("Aio Seller", itemId, 1, player.getTarget(), true);

			doAio(player, 90);
	}
		else if (command.startsWith("remove_aio"))
		{
			removeAio(player);
		}
		showHtmlWindow(player);
	}

	private void showHtmlWindow(L2PcInstance activeChar)
	{
		NpcHtmlMessage nhm = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("");

		replyMSG.append("<html><title>Aio Seller NPC</title><body><center>");
		replyMSG.append("<img src=L2UI_CH3.herotower_deco width=256 height=32><br>");
		replyMSG.append("<font color=LEVEL>NPC Aio Seller</font><br>");
		replyMSG.append("<img src=L2UI.SquareGray width=300 height=1><br>");
		replyMSG.append("<center><button value=\"1 month\" action=\"bypass -h npc_" + getObjectId() + "_add_aio_1\" width=205 height=20 back=\"sek.cbui81\" fore=\"sek.cbui79\"></center>");
		replyMSG.append("<center><button value=\"2 months\" action=\"bypass -h npc_" + getObjectId() + "_add_aio_2\" width=205 height=20 back=\"sek.cbui81\" fore=\"sek.cbui79\"></center>");
		replyMSG.append("<center><button value=\"3 months\" action=\"bypass -h npc_" + getObjectId() + "_add_aio_3\" width=205 height=20 back=\"sek.cbui81\" fore=\"sek.cbui79\"></center><br>");
		replyMSG.append("<img src=L2UI_CH3.herotower_deco width=256 height=32><br>");
		replyMSG.append("<center><button value=\"Remove\" action=\"bypass -h npc_" + getObjectId() + "_remove_aio\" width=205 height=20 back=\"sek.cbui81\" fore=\"sek.cbui79\"></center><br>");
		replyMSG.append("<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>");
		replyMSG.append("<img src=l2ui.bbs_lineage2 height=16 width=80>");
		replyMSG.append("<font color=808080>" + Config.ALT_Server_Name + "</font>");
		replyMSG.append("</center></body></html>");

		nhm.setHtml(replyMSG.toString());
		activeChar.sendPacket(nhm);

		activeChar.sendPacket(new ActionFailed());
	}

	public void doAio(L2PcInstance player, int days)
	{
		if (player == null)
			return;

		player.setAio(true);
		player.setEndTime("aio", days);
		player.getStat().addExp(player.getStat().getExpForLevel(81));

		Connection connection = null;
		try
		{
			connection = L2DatabaseFactory.getInstance().getConnection(false);

			PreparedStatement statement = connection.prepareStatement("UPDATE characters SET aio=1, aio_end=? WHERE obj_id=?");
			statement.setLong(1, player.getAioEndTime());
			statement.setInt(2, player.getObjectId());
			statement.execute();
			statement.close();
			connection.close();

			if (Config.ALLOW_AIO_NCOLOR && player.isAio())
			player.getAppearance().setNameColor(Config.AIO_NCOLOR);

			if (Config.ALLOW_AIO_TCOLOR && player.isAio())
				player.getAppearance().setTitleColor(Config.AIO_TCOLOR);

			player.rewardAioSkills();
			player.broadcastUserInfo();
			player.sendPacket(new EtcStatusUpdate(player));
			player.sendSkillList();
			player.sendMessage("You are now an Aio, Congratulations!");
			player.broadcastUserInfo();
		}
	catch (Exception e)
		{
			if (Config.ENABLE_ALL_EXCEPTIONS)
				e.printStackTrace();

			_log.log(Level.WARNING, "could not set Aio stats to char:", e);
		}
		finally
		{
		CloseUtil.close(connection);
		}
	}

	public void removeAio(L2PcInstance player)
	{
		player.setAio(false);
		player.setAioEndTime(0);

	Connection connection = null;
		try
		{
			connection = L2DatabaseFactory.getInstance().getConnection(false);

			PreparedStatement statement = connection.prepareStatement("UPDATE characters SET Aio=0, Aio_end=0 WHERE obj_id=?");
			statement.setInt(1, player.getObjectId());
			statement.execute();
			statement.close();
		connection.close();

			player.lostAioSkills();
			player.getAppearance().setNameColor(0xFFFFFF);
			player.getAppearance().setTitleColor(0xFFFF77);
			player.broadcastUserInfo();
			player.sendPacket(new EtcStatusUpdate(player));
			player.sendSkillList();
			player.sendMessage("Now You are not an Aio..");
			player.broadcastUserInfo();
		}
		catch (Exception e)
		{
			if (Config.ENABLE_ALL_EXCEPTIONS)
				e.printStackTrace();

			_log.log(Level.WARNING, "could not remove Aio stats of char:", e);
		}
		finally
		{
			CloseUtil.close(connection);
		}
	}
}