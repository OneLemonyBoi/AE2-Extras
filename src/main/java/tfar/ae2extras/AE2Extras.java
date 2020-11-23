package tfar.ae2extras;

import appeng.block.crafting.AbstractCraftingUnitBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AE2Extras.MODID)
public class AE2Extras
{
    // Directly reference a log4j logger.

    public static final String MODID = "ae2extras";

    public static AbstractCraftingUnitBlock.CraftingUnitType STORAGE_256K;
    public static AbstractCraftingUnitBlock.CraftingUnitType STORAGE_1M;
    public static AbstractCraftingUnitBlock.CraftingUnitType STORAGE_4M;
    public static AbstractCraftingUnitBlock.CraftingUnitType STORAGE_16M;

    public static Block STORAGE256K;
    public static Block STORAGE1M;
    public static Block STORAGE4M;
    public static Block STORAGE16M;

    public static final ItemGroup TAB = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return STORAGE16M.asItem().getDefaultInstance();
        }
    };

    public AE2Extras() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        bus.addGenericListener(Block.class,this::blocks);
        bus.addGenericListener(Item.class,this::items);
        bus.addListener(this::client);
    }

    private void client(FMLClientSetupEvent t) {
        RenderTypeLookup.setRenderLayer(STORAGE256K, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(STORAGE1M, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(STORAGE4M, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(STORAGE16M, RenderType.getCutout());

    }

    private void blocks(final RegistryEvent.Register<Block> event) {

        AbstractBlock.Properties craftingBlockProps = defaultProps(Material.IRON,MaterialColor.GRAY);

        STORAGE256K = register(event.getRegistry(), "256k_crafting_storage",new CraftingStorageBlockEx(craftingBlockProps,STORAGE_256K));
        STORAGE1M = register(event.getRegistry(), "1m_crafting_storage",new CraftingStorageBlockEx(craftingBlockProps, STORAGE_1M));
        STORAGE4M = register(event.getRegistry(), "4m_crafting_storage",new CraftingStorageBlockEx(craftingBlockProps, STORAGE_4M));
        STORAGE16M = register(event.getRegistry(), "16m_crafting_storage",new CraftingStorageBlockEx(craftingBlockProps, STORAGE_16M));

    }

    private void items(final RegistryEvent.Register<Item> event) {
        Item.Properties props = new Item.Properties().group(TAB);
        register(event.getRegistry(),STORAGE256K.getRegistryName(),new BlockItem(STORAGE256K,props));
        register(event.getRegistry(), STORAGE1M.getRegistryName(),new BlockItem(STORAGE1M,props));
        register(event.getRegistry(), STORAGE4M.getRegistryName(),new BlockItem(STORAGE4M,props));
        register(event.getRegistry(), STORAGE16M.getRegistryName(),new BlockItem(STORAGE16M,props));
    }

    private static <T extends IForgeRegistryEntry<T>> T register(IForgeRegistry<T> registry, ResourceLocation name, T obj) {
        registry.register(obj.setRegistryName(name));
        return obj;
    }

    private static <T extends IForgeRegistryEntry<T>> T register(IForgeRegistry<T> registry, String name, T obj) {
        register(registry, new ResourceLocation(MODID, name), obj);
        return obj;
    }

    /**
     * Utility function to create block properties with some sensible defaults for
     * AE blocks.
     */
    public static Block.Properties defaultProps(Material material, MaterialColor color) {
        return Block.Properties.create(material, color)
                // These values previousls were encoded in AEBaseBlock
                .hardnessAndResistance(2.2f, 11.f).harvestTool(ToolType.PICKAXE).harvestLevel(0)
                .sound(SoundType.METAL);
    }
}