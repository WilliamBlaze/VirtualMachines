package com.arcaratus.virtualmachines.gui.client.machine;

import cofh.core.gui.element.*;
import cofh.thermalexpansion.gui.client.GuiPoweredBase;
import cofh.thermalexpansion.gui.element.ElementSlotOverlay;
import cofh.thermalexpansion.gui.element.ElementSlotOverlay.*;
import com.arcaratus.virtualmachines.block.machine.TileMobSpawner;
import com.arcaratus.virtualmachines.gui.container.machine.ContainerMobSpawner;
import com.arcaratus.virtualmachines.gui.element.ElementSlotOverlay3x3;
import com.arcaratus.virtualmachines.init.VMConstants;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiMobSpawner extends GuiPoweredBase
{
    public static final String TEX_PATH = VMConstants.PATH_MACHINE_GUI + "mob_spawner.png";
    public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

    private ElementSlotOverlay[] slotInput = new ElementSlotOverlay[2];
    private ElementSlotOverlay[] slotMorb = new ElementSlotOverlay[2];
    private ElementSlotOverlay3x3 slotOutput;
    private ElementSlotOverlay[] slotTank = new ElementSlotOverlay[2];

    private ElementSimple tankBackground;
    private ElementFluidTank tank;

    private ElementDualScaled progress;
    private ElementDualScaled progressOverlay;
    private ElementFluid progressFluid;

    private TileMobSpawner myTile;

    public GuiMobSpawner(InventoryPlayer inventory, TileEntity tile)
    {
        super(new ContainerMobSpawner(inventory, tile), tile, inventory.player, TEXTURE);

        generateInfo(VMConstants.MACHINE_GUI_INFO + "mob_spawner");

        myTile = (TileMobSpawner) tile;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        tankBackground = (ElementSimple) addElement(new ElementSimple(this, 151, 8).setTextureOffsets(176, 104).setSize(18, 62).setTexture(TEX_PATH, 256, 256));

        slotInput[0] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(this, 38, 35).setSlotInfo(SlotColor.BLUE, SlotType.STANDARD, SlotRender.FULL));
        slotInput[1] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(this, 38, 35).setSlotInfo(SlotColor.GREEN, SlotType.STANDARD, SlotRender.FULL));
        slotMorb[0] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(this, 66, 25).setSlotInfo(SlotColor.BLUE, SlotType.STANDARD, SlotRender.FULL));
        slotMorb[1] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(this, 66, 25).setSlotInfo(SlotColor.PURPLE, SlotType.STANDARD, SlotRender.FULL));
        slotOutput = (ElementSlotOverlay3x3) addElement(new ElementSlotOverlay3x3(this, 92, 17).setSlotInfo(SlotColor.ORANGE, SlotRender.FULL));

        slotTank[0] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(this, 152, 9).setSlotInfo(SlotColor.ORANGE, SlotType.TANK, SlotRender.FULL).setVisible(false));
        slotTank[1] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(this, 152, 9).setSlotInfo(SlotColor.YELLOW, SlotType.TANK, SlotRender.BOTTOM).setVisible(false));

        if (!myTile.smallStorage())
            addElement(new ElementEnergyStored(this, 8, 8, myTile.getEnergyStorage()));

        tank = (ElementFluidTank) addElement(new ElementFluidTank(this, 152, 9, myTile.getTank()).setGauge(1).setAlwaysShow(true));

        progress = (ElementDualScaled) addElement(new ElementDualScaled(this, 62, 44).setMode(1).setSize(24, 16).setTexture(TEX_ARROW_RIGHT, 64, 16));
        progressFluid = (ElementFluid) addElement(new ElementFluid(this, 62, 44).setFluid(myTile.getTankFluid()).setSize(24, 16));
        progressOverlay = (ElementDualScaled) addElement(new ElementDualScaled(this, 62, 44).setMode(1).setBackground(false).setSize(24, 16).setTexture(TEX_ARROW_FLUID_RIGHT, 64, 16));

        slotTank[0].setVisible(myTile.augmentExperience());
        slotTank[1].setVisible(myTile.augmentExperience());

        tankBackground.setVisible(myTile.augmentExperience());
        tank.setVisible(myTile.augmentExperience());
        progressFluid.setVisible(myTile.augmentExperience());
        progressOverlay.setVisible(myTile.augmentExperience());
    }

    @Override
    protected void updateElementInformation()
    {
        super.updateElementInformation();

        slotInput[0].setVisible(baseTile.hasSideType(INPUT_ALL) || baseTile.hasSideType(OMNI));
        slotInput[1].setVisible(baseTile.hasSideType(INPUT_PRIMARY));
        slotMorb[0].setVisible(baseTile.hasSideType(INPUT_ALL) || baseTile.hasSideType(OMNI));
        slotMorb[1].setVisible(baseTile.hasSideType(INPUT_SECONDARY));
        slotOutput.setVisible(baseTile.hasSideType(OUTPUT_ALL) || baseTile.hasSideType(OMNI));

        slotTank[0].setVisible(myTile.augmentExperience() && (baseTile.hasSideType(OUTPUT_ALL) || baseTile.hasSideType(OMNI)));
        slotTank[1].setVisible(myTile.augmentExperience() && baseTile.hasSideType(OUTPUT_SECONDARY));

        slotTank[1].setSlotRender(SlotRender.BOTTOM);

        if (!baseTile.hasSideType(INPUT_ALL))
        {
            slotInput[1].setSlotRender(SlotRender.FULL);
            slotMorb[1].setSlotRender(SlotRender.FULL);
        }
        else
        {
            slotInput[1].setSlotRender(SlotRender.BOTTOM);
            slotMorb[1].setSlotRender(SlotRender.BOTTOM);
        }

        progress.setQuantity(baseTile.getScaledProgress(PROGRESS));

        progressFluid.setSize(baseTile.getScaledProgress(PROGRESS), 16);
        progressOverlay.setQuantity(baseTile.getScaledProgress(PROGRESS));

        progress.setVisible(!myTile.augmentExperience());

        tankBackground.setVisible(myTile.augmentExperience());
        tank.setVisible(myTile.augmentExperience());
        progressFluid.setVisible(myTile.augmentExperience());
        progressOverlay.setVisible(myTile.augmentExperience());
    }
}