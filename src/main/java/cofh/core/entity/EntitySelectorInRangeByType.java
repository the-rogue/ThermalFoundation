package cofh.core.entity;

import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;

@Deprecated
public class EntitySelectorInRangeByType extends EntitySelector {

	private final double origX;
	private final double origY;
	private final double origZ;
	private final double distance;
	private final Class<?> types[];

	public EntitySelectorInRangeByType(Entity origin, double distance, Class<?>... types) {

		this(origin.posX, origin.posY, origin.posZ, distance, types);
	}

	public EntitySelectorInRangeByType(double originX, double originY, double originZ, double distance, Class<?>... types) {

		origX = originX;
		origY = originY;
		origZ = originZ;
		this.distance = distance;
		this.types = types;
	}

	public boolean isEntityApplicable(Entity entity) {

		// Out of range? Not applicable.
		if (entity.getDistanceSq(origX, origY, origZ) > distance * distance) {
			return false;
		}
		// No specific types to check for? Applicable.
		if (types == null) {
			return true;
		}
		// Check types. Applicable if found and assignable...
		for (Class<?> type : types) {
			if (type.isAssignableFrom(entity.getClass())) {
				return true;
			}
		}
		// ...otherwise, not.
		return false;
	}

}
