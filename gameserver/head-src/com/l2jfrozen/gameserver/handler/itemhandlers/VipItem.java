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
 * this program. If not, see .
 */
package com.l2jfrozen.gameserver.handler.itemhandlers;

import com.l2jfrozen.Config;
import com.l2jfrozen.gameserver.handler.IItemHandler;
import com.l2jfrozen.gameserver.model.actor.instance.L2ItemInstance;
import com.l2jfrozen.gameserver.model.actor.instance.L2PlayableInstance;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Paulinho Souza
 * @author KhayrusS
 **/

public class VipItem implements IItemHandler
{
 private static final int ITEM_IDS[] = { Config.VIP_ITEM };

 @Override
 public synchronized void useItem(L2PlayableInstance playable,
 L2ItemInstance item) {
 if(!(playable instanceof L2PcInstance))
 return;

 L2PcInstance player = (L2PcInstance)playable;

 if (player.isInOlympiadMode())
 player.sendMessage("Voce nao pode usar este item em olimpiadas.");

 else if (player.isVip()) {
 player.sendMessage("Voce ainda esta no periodo de VIP, espere ele terminar para poder usar de novo.");
 return;
 }

 else
 {
 if(playable.destroyItem("Consume", item.getObjectId(), 1, null,
 false)) {
 player.setVip(true);
 player.setEndTime("vip", Config.VIP_DIAS);
 player.sendMessage("Voce se tornou um Vip temporario! Voce tera privilegios de acessar Npcs Vips. Seu Vip ira durar ate voce deslogar sua conta!");

 player.broadcastUserInfo();
 }
 }
 }

 @Override
 public int[] getItemIds()
 {
 return ITEM_IDS;
 }
}