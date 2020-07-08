import org.joml.Vector3f;

public class Light {

	private int type;             // type 1 = directional, type 2 = point
	private Vector3f location;
	private Vector3f direction;
	private Vector3f color;
	private float amount;
	
	
	public Light(int type, Vector3f locationOrDirection, Vector3f color, float amount) {
		
		this.type = type;
		
		this.location = locationOrDirection;
		this.direction = locationOrDirection;
		this.color = color;
		this.amount = amount;
	}
	/*
	public Light(int type, Vector3f direction, Vector3f color, float amount) {
		
	}
*/
	public Vector3f getLocation() {
		return location;
	}

	public void setLocation(Vector3f location) {
		this.location = location;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
}
