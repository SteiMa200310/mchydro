public VoxelShape makeShape(){
	VoxelShape shape = VoxelShapes.empty();
	shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.12499999999999989, 0, 0.125, 0.8749999999999999, 0.125, 0.875));
	shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.125, 0.25, 0.75, 0.5, 0.75));
	shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.12499999999999989, 0.5, 0.125, 0.8749999999999999, 0.625, 0.875));

	return shape;
}