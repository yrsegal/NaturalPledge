package shadowfox.botanicaladdons.api.priest;

import net.minecraft.item.ItemStack;
import shadowfox.botanicaladdons.api.priest.IFocusSpell;

import javax.annotation.Nonnull;

public class SpellRecipe {

    @Nonnull
    private String input;
    @Nonnull
    private ItemStack output;
    @Nonnull
    private IFocusSpell spell;

    public SpellRecipe(@Nonnull String input, @Nonnull ItemStack output, @Nonnull IFocusSpell spell) {
        this.input = input;
        this.output = output;
        this.spell = spell;
    }

    @Nonnull
    public String getInput() {
        return input;
    }

    @Nonnull
    public ItemStack getOutput() {
        return output;
    }

    @Nonnull
    public IFocusSpell getSpell() {
        return spell;
    }
}
