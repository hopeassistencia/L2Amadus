package com.l2jfrozen.gameserver.handler.voicedcommandhandlers;

import javolution.text.TextBuilder;

import com.l2jfrozen.gameserver.handler.IVoicedCommandHandler;
import com.l2jfrozen.gameserver.model.actor.instance.L2PcInstance;
import com.l2jfrozen.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author te0x
 */
@SuppressWarnings("unused")
public class CastleManagersVCmd implements IVoicedCommandHandler
{
       private static final String[] VOICED_COMMANDS =
       {
               "castle",
       };
      
       @Override
       public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
       {
               if (command.startsWith("castle")) {
                       sendHtml(activeChar);
               }
               return true;
       }
      
       private void sendHtml(L2PcInstance activeChar)
       {
               String htmFile = "data/html/mods/CastleManager.htm";
              
               NpcHtmlMessage msg = new NpcHtmlMessage(5);
               msg.setFile(htmFile);
               activeChar.sendPacket(msg);
       }
      
       @Override
       public String[] getVoicedCommandList()
       {
               return VOICED_COMMANDS;
       }
} 