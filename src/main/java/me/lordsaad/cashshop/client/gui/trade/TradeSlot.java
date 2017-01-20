package me.lordsaad.cashshop.client.gui.trade;

import com.teamwizardry.librarianlib.client.gui.GuiComponent;
import com.teamwizardry.librarianlib.client.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.client.gui.components.ComponentText;
import com.teamwizardry.librarianlib.client.gui.components.ComponentVoid;
import com.teamwizardry.librarianlib.client.gui.mixin.ButtonMixin;
import com.teamwizardry.librarianlib.client.sprite.Sprite;
import com.teamwizardry.librarianlib.client.sprite.Texture;
import com.teamwizardry.librarianlib.common.util.math.interpolate.position.InterpLine;
import me.lordsaad.cashshop.api.Constants;
import me.lordsaad.cashshop.api.capability.CapabilityWallet;
import me.lordsaad.cashshop.api.capability.IWalletCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.List;

/**
 * Created by LordSaad.
 */
public class TradeSlot {

	public static final Sprite sprBuyNormal = GuiTrade.spriteSheet.getSprite("buy_normal", 26, 24);
	public static final Sprite sprBuyHighlighted = GuiTrade.spriteSheet.getSprite("buy_highlighted", 26, 24);
	public static final Sprite sprBuyLocked = GuiTrade.spriteSheet.getSprite("buy_locked", 26, 24);
	public static final Sprite coin = new Texture(new ResourceLocation(Constants.MOD_ID, "textures/items/currency_coin.png")).getSprite("coin", 16, 16);

	public ComponentVoid component;

	public TradeSlot(GuiTrade base, GuiTrade.TradeInfo tradeInfo) {
		ComponentVoid plate = new ComponentVoid(0, 0, 64, 24);
		plate.setMarginBottom(6);

		ComponentSprite buyButton = new ComponentSprite(sprBuyNormal, 40, 0);
		new ButtonMixin<>(buyButton, () -> {
		});
		buyButton.BUS.hook(GuiComponent.ComponentTickEvent.class, (event) -> {
			if (tradeInfo.cost <= base.wallet) {
				if (event.getComponent().getMouseOver())
					buyButton.setSprite(sprBuyHighlighted);
				else buyButton.setSprite(sprBuyNormal);
			} else buyButton.setSprite(sprBuyLocked);
		});
		buyButton.BUS.hook(GuiComponent.MouseClickEvent.class, (event) -> {
			if (tradeInfo.cost > base.wallet) return;

			IWalletCapability wallet = Minecraft.getMinecraft().player.getCapability(CapabilityWallet.WALLET, null);
			wallet.setWallet(base.wallet - tradeInfo.cost);

			for (ItemStack stack : tradeInfo.outputs)
				Minecraft.getMinecraft().player.inventory.addItemStackToInventory(stack.copy());
		});
		plate.add(buyButton);

		ComponentSprite coinIcon = new ComponentSprite(coin, -5, -6);
		plate.add(coinIcon);

		ComponentText costText = new ComponentText(8, 3, ComponentText.TextAlignH.LEFT, ComponentText.TextAlignV.MIDDLE);
		costText.getText().setValue(tradeInfo.cost + "");
		costText.getColor().setValue(Color.WHITE);
		plate.add(costText);

		InterpLine line = new InterpLine(new Vec3d(0, 5, 0), new Vec3d(24, 5, 0));
		List<Vec3d> list = line.list(tradeInfo.outputs.size());
		for (ItemStack output : tradeInfo.outputs) {
			Vec3d point = list.get(tradeInfo.outputs.indexOf(output));
			//ComponentSlot slot = new ComponentSlot(new SlotBase(IItemHandlerModifiable.)) new ComponentSlot((int) point.xCoord, (int) point.yCoord);
			//slot.getStack().setValue(output);
			//plate.add(slot);
		}

		component = plate;
	}
}
