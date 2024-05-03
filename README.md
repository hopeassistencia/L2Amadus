# L2Amadus Interlude Private Server</h3>
## Site Oficial (desativado):
https://l2amadus.servegame.com/

# Informações sobre os Mods
## Anúncios
**Announce Hero Login** - Anuncia quando os Heróis entram no servidor.<br>
**Announce RaidBoss** - Anuncia quando um RaidBoss morre, quanto ele dropou e quando ele renasce<br>
**Players Online Fake** - Anuncia a quantidade de players online + uma quantidade falsa configurada no arquivo [l2jfrozen.properties](gameserver/config/functions/l2jfrozen.properties)
```properties
Announcia os players ON ao logar.
ShowOnlinePlayersAtStartup = True
OnlinePlayerCountTrick = 10
```
## Comandos
**[ADMIN]//senddonate** - Envia doações aos players online<br>
**[ADMIN]//clanfull** - Adiciona todas Skills + lvl + reputação no clan selecionado<br>
**[ADMIN]//masshero** - Adiciona status herói em todos os player online no servidor até o restart<br>
**[ADMIN]//setvip <char_name> [time](in days)** - Adiciona status VIP em personagem selecionado<br>
**[PLAYER].castle** - Líderes de clan podem cadastrar para Sieges com este comando<br>

## Eventos
**Trivia** - O Evento envia perguntas que são configuradas no arquivo [Trivia.xml](gameserver/config/Trivia.xml), então o player responde no pm para o evento, exemplo:
```
Trivia resposta
```
A recompensa deve ser configurada no arquivo [Trivia.java](gameserver/head-src/com/l2jfrozen/gameserver/model/entity/event/Trivia.java), na opção:

```java
private static int _rewardID = 9142; // Id da recompensa
private static int _rewardCount = 1; // Quantidade da recompensa
```
**Remover Tutorial** - Remove a introdução e exibição de tutorial quando se cria um novo personagem<br>
**NPCCrest** - Adiciona bandeira do clan dono do castelo regente da região em todos os NPCs daquela região.<br>
**Lost Boss** - Evento que faz aparecer um Boss em localização determinada no arquivo [l2jfrozen.properties](gameserver/config/functions/l2jfrozen.properties). Os horários também são ajustados no mesmo arquivo.<br>
**HTML EVENT** - Modificado HTML dos principais eventos: TVT / DM / CTF<br>

## Itens
**Hero** - Dois cliques no item para se tornar Hero até o restart do servidor. Configurações: [other.properties](gameserver/config/other.propierts)<br>
**VIP** - Dois cliques no item para se tornar VIP pelo tempo determinado em configuração. Configurações: [other.properties](gameserver/config/other.propierts)<br>

## Proteção
**Anti DualBox** - Limita o número de DualBox.
Configurações: [other.properties](gameserver/config/protected/other.propierts)
```propierts
MultiBoxesPerPC = 2
```
**Anti L2Walker** - Proteção contra L2Walker.Configurações: [other.properties](gameserver/config/protected/other.propierts)
```propierts
L2WalkerProtection = False
```
**Anti PHX** - Proteção adicional contra L2PHX<br>
**Anti Aio Out Of City** - Impede personagem buffer AIO de sair da cidade e participar de eventos.<br>
**Anti Trade in FightMode** - Impede trade em personagens que estão lutando ou em modo de batalha<br>
**Anti Trade in PartyMode** - Impede trade em personagens que estão em modo de party<br>
**[ADMIN] Dye View** - Permite o Admin ver as dyes utilizadas por cada personagem, usando o comando **alt+g**<br>
**Anti Exploit** - Proteção contra dual login.<br>
**Auto Learn Skill** - Correção de duplicação de skill's com acumulo de subclass<br>

## Outros
**AioSeller** - NPC que vende Aio por item.
[other.properties](gameserver/config/head/other.properties)
```properties
AioItemId = 3470
```
**AntAFK** - Desconecta personagem que está inativo. [other.properties](gameserver/config/protected/other.propierts)
```properties
LeaveBursterTimeKick = 10
```
**Auto Restart** - Restart automatico do servidor configurado no horário.
[l2jfrozen.properties](gameserver/config/functions/l2jfrozen.properties)
```properties
RestartByTimeOfDay = 00:00,12:00
```
**Max Players in Clan** - Configura número máximo de personagens na base principal dos clans.
[altsettings.properties](gameserver/config/head/altsettings.properties)
```properties
AltMaxNumOfMembersInClan = 20
```
