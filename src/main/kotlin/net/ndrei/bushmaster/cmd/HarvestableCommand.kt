package net.ndrei.bushmaster.cmd

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentString
import net.ndrei.bushmaster.api.BushMasterApi

object HarvestableCommand: CommandBase() {
    override fun getName() = "harvestables"

    override fun getUsage(sender: ICommandSender) = "/harvestables <-- will find all harvestable blocks around the player"

    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<out String>?) {
        var found = false
        val onlyHarvestable = args?.any { it == "-f" } ?: false
        val harvest = args?.any { it == "-h" } ?: false
        for(x in -10..10) {
            for (y in -5..5) {
                for (z in -10..10) {
                    val pos = sender.position.add(x, y, z)
                    val harvestable = BushMasterApi.harvestableFactory.getHarvestable(sender.entityWorld, pos)
                    if (harvestable != null) {
                        val state = sender.entityWorld.getBlockState(pos)!!
                        val canBeHarvested = harvestable.canBeHarvested(sender.entityWorld, pos, state)
                        found = true

                        if (canBeHarvested || !onlyHarvestable) {
                            sender.sendMessage(TextComponentString("Harvestable '${
                            state.block?.registryName ?: "[no registry name]"
                            }' at: $pos, can be harvested: $canBeHarvested${
                            if (canBeHarvested) {
                                " -> " +
                                    harvestable.harvest(sender.entityWorld, pos, state, harvest)
                                    .joinToString(", ") { it.toString() }
                            } else ""
                            }."))
                        }
                    }
                }
            }
        }
        if (!found) {
            sender.sendMessage(TextComponentString("No harvestable block found!"))
        }
    }
}