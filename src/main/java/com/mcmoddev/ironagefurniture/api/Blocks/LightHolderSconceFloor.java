package com.mcmoddev.ironagefurniture.api.Blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

public class LightHolderSconceFloor extends LightHolderSconce {
	public LightHolderSconceFloor(Properties properties) {
		super(properties);
		
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
		// TODO Auto-generated constructor stub
	}
	
	public LightHolderSconceFloor(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos)
	{
		return canSupportCenter(levelReader, pos.below(), Direction.UP);
	}
	
	@Override
	protected void generateShapes(ImmutableList<BlockState> states) {
		 ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
	        for(BlockState state : states)
	        {
	        	VoxelShape shapes = Shapes.empty();
	        
	        	// chair body                                                      X1 Y1 Z1 X2  Y2 Z2
	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(1, 7, 1, 15, 8, 14), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // chair base
	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(3, 9, 1, 13, 23, 2), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // chair back
	        	
//	        	//legs
//	        	shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(2, 0, 12, 3, 8, 13), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); //front left leg
//	            shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(13, 0, 12, 14, 8, 13), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // front right leg
//	            shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(1, 0, 1, 3, 22, 3), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // back left leg
//	            shapes = Shapes.joinUnoptimized(shapes, getShapes(rotate(Block.box(13, 0, 1, 15, 22, 3), Direction.SOUTH))[state.getValue(DIRECTION).get2DDataValue()], BooleanOp.OR); // back right leg
	            
	            builder.put(state, shapes.optimize());
	        }
	        
	        _shapes = builder.build();
	}

	@Override
	protected InteractionResult ActivateSconce(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult rayTraceResult) {
		
		ItemStack stackInHand = player.getItemInHand(hand);
		
		if (stackInHand.is(Blocks.TORCH.asItem())) {
			world.setBlock(pos, BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron.defaultBlockState()
					.setValue(DIRECTION, state.getValue(BlockStateProperties.HORIZONTAL_FACING))
					.setValue(WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)), UPDATE_ALL);
			
			if (!player.isCreative())
				stackInHand.setCount(stackInHand.getCount()-1);;
			
				return InteractionResult.CONSUME_PARTIAL;
		}
		
		return InteractionResult.FAIL;
	}
	
	@Override
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
    	List<ItemStack> drops;
    	
    	Item item = state.getBlock().asItem();
    	ItemStack stack = new ItemStack(item, 1); 
    	drops = new ArrayList<ItemStack>();
    	drops.add(stack);
    	
    	return drops;
    }

	@Override
	protected boolean onPlaceLiquid(LevelAccessor world, BlockPos pos, BlockState blockState, FluidState fluidState) {
		return false;
	}
}

/*package com.mcmoddev.ironagefurniture.api.Blocks;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public class LightHolderSconceFloor extends LightHolderSconce {

	private static final AxisAlignedBB NORTHBB = new AxisAlignedBB(0.3, 0.0, 0.2, 0.7, 0.7, 0.7);

	public LightHolderSconceFloor(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn, name, resistance, hardness);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected IBlockState getTorchVariant(IBlockState initialState) {
		return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron.getDefaultState()
				.withProperty(FACING, (EnumFacing)initialState.getProperties().get(FACING));
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		if (isSolidBlock(worldIn.getBlockState(pos.offset(EnumFacing.DOWN))))
			return true;
		
		if (isSolidBlock(worldIn.getBlockState(pos.offset(EnumFacing.NORTH))))
			return true;
		
		if (isSolidBlock(worldIn.getBlockState(pos.offset(EnumFacing.EAST))))
			return true;
		
		if (isSolidBlock(worldIn.getBlockState(pos.offset(EnumFacing.SOUTH))))
			return true;
		
		if (isSolidBlock(worldIn.getBlockState(pos.offset(EnumFacing.WEST))))
			return true;
	
		return false;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		
		IBlockState defaultState = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack)
				.withProperty(FACING, placer.getHorizontalFacing());
		
		if (isSolidBlock(world.getBlockState(pos.offset(EnumFacing.DOWN))))
			return defaultState;
		
		if (!isSolidBlock(world.getBlockState(pos.offset(placer.getHorizontalFacing())))) {
			if (isSolidBlock(world.getBlockState(pos.offset(EnumFacing.NORTH))))
				return this.getOppositeVariant(EnumFacing.NORTH);
			
			if (isSolidBlock(world.getBlockState(pos.offset(EnumFacing.EAST))))
				return this.getOppositeVariant(EnumFacing.EAST);
			
			if (isSolidBlock(world.getBlockState(pos.offset(EnumFacing.SOUTH))))
				return this.getOppositeVariant(EnumFacing.SOUTH);
			
			if (isSolidBlock(world.getBlockState(pos.offset(EnumFacing.WEST))))
				return this.getOppositeVariant(EnumFacing.WEST);
			
			return defaultState;
		}
		
		return this.getOppositeVariant(defaultState);
	}

	@Override
	protected IBlockState getOppositeVariant(IBlockState initialState) {
		return BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron.getDefaultState().withProperty(FACING, (EnumFacing)initialState.getProperties().get(FACING));
	}
	
	@Override
	protected AxisAlignedBB getNorthBB() {
		return NORTHBB;
	}

	@Override
	protected AxisAlignedBB getEastBB() {
		return getEast(NORTHBB);
	}

	@Override
	protected AxisAlignedBB getSouthBB() {
		return getSouth(NORTHBB);
	}

	@Override
	protected AxisAlignedBB getWestBB() {
		return getWest(NORTHBB);
	}	
}
*/