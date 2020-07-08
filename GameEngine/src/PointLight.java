import org.joml.Vector3f;

public class PointLight {

	private Vector3f location;
	private Vector3f color;
	private float strength;
	private float radius;
	
	public PointLight(Vector3f location, Vector3f color, float strength, float radius) {
		
		this.location = location;
		this.color = color;
		this.strength = strength;
		this.radius = radius;
	}

	public Vector3f getLocation() {
		return location;
	}

	public void setLocation(Vector3f location) {
		this.location = location;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
}
