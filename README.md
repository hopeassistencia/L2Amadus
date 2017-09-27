<h3> L2Amadus Interlude Private Server</h3><br>
<br>
<br>
Site Oficial:<br>
https://l2amadus.servegame.com/<br>
<br>
<h4>Informações sobre os Mods:</h4><br>
<b>Anúncios:</b><br>
Announce Hero Login - Anuncia quando os Heróis entram no servidor.<br>
Announce RaidBoss - Anuncia quando um RaidBoss morre, quanto ele dropou e quando ele renasce<br>
Players Online Fake - Anuncia a quantidade de players online + uma quantidade falsa configurada no arquivo config/functions/l2jfrozen.properties:<br>
Announcia os players ON ao logar.<br>
ShowOnlinePlayersAtStartup = True<br>

Isto irá adicionar o número abaixo para definir a quantidade real de jogadores online. (talvez seja necessário desativar essa página no Conselho da Comunidade, se você fizer isso!)<br>
OnlinePlayerCountTrick = 10<br>
<br>

<b>Comandos:</b><br>
[ADMIN]//senddonate - Envia doações aos players online<br>
[ADMIN]//clanfull - Adiciona todas Skills + lvl + reputação no clan selecionado<br>
[ADMIN]//masshero - Adiciona status herói em todos os player online no servidor até o restart<br>
[ADMIN]//setvip <char_name> [time](in days) - Adiciona status VIP em personagem selecionado<br>
[PLAYER].castle - Líderes de clan podem cadastrar para Sieges com este comando<br>

<b>Eventos:</b><br>
Trivia - O Evento envia perguntas que são configuradas no arquivo gameserver/config/Trivia.xml, então o player responde no pm para o evento, exemplo: "Trivia resposta. A recompensa deve ser configurada no arquivo head-src/com/l2jfrozen/gameserver/model/entity/event/Trivia.java, na opção: "private static int _rewardID = 9142;" e a quantidade em "private static int _rewardCount = 1;".<br>
Remover Tutorial - Remove a introdução e exibição de tutorial quando se cria um novo personagem<br>
NPCCrest - Adiciona bandeira do clan dono do castelo regente da região em todos os NPCs daquela região.<br>
Lost Boss - Evento que faz aparecer um Boss em localização determinada no arquivo "config/frozen/frozen.properties". Os horários também são ajustados no mesmo arquivo.<br>
HTML EVENT - Modificado HTML dos principais eventos: TVT / DM / CTF<br>

<b>Itens:</b><br>
Hero - Dois cliques no item para se tornar Hero até o restart do servidor. Editar arquivo: "config/others/other.properties"<br>
VIP - Dois cliques no item para se tornar VIP pelo tempo determinado em configuração. Editar aquivo: "config/head/other.properties"<br>

<b>Proteção</b><br>
Anti DualBox - Limita o número de DualBox via configuração: "config/protected/other.properties" ->"MultiBoxesPerPC = 2"<br>
Anti L2Walker - Proteção contra L2Walker via configuração: "gameserver/config/protected/other.properties" -> "L2WalkerProtection = False"<br>
Anti PHX - Proteção adicional contra L2PHX<br>
Anti Aio Out Of City - Impede personagem buffer AIO de sair da cidade e participar de eventos.<br>
Anti Trade in FightMode - Impede trade em personagens que estão lutando ou em modo de batalha<br>
Anti Trade in PartyMode - Impede trade em personagens que estão em modo de party<br>
[ADMIN]Dye View - Permite o Admin ver as dyes utilizadas por cada personagem, usando o comando alt+g<br>
Anti Exploit - Proteção contra dual login.<br>
Auto Learn Skill - Correção de duplicação de skill's com acumulo de subclass<br>

<b>Outros:</b><br>
AioSeller - NPC que vende Aio por item. Configuração no arquivo: "config/head/other.properties" -> "AioItemId = 3470"<br>
AntAFK - Desconecta personagem que está inativo. Configuração no arquivo: "config/protected/other.properties" -> "LeaveBursterTimeKick = 10"<br>
Auto Restart - Restart automatico do servidor configurado no horário editado no arquivo: "gameserver/config/functions/l2jfrozen.properties" -> "RestartByTimeOfDay = 00:00,12:00"<br>
Max Players in Clan - Configura número máximo de personagens na base principal dos clans. Configurar em: "gameserver\config\head\altsettings.properties" -> "AltMaxNumOfMembersInClan = 20"<br>
