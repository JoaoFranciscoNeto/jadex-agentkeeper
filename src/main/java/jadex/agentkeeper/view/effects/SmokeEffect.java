package jadex.agentkeeper.view.effects;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class SmokeEffect extends ParticleEmitter
{

	public SmokeEffect(AssetManager assetManager)
	{
		super("Smoke", Type.Triangle, 10);
		Material material = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		material.setTexture("Texture", assetManager.loadTexture("jadex3d/textures/effects/explosion/flame.png"));
		material.setFloat("Softness", 6f); //

		this.setParticlesPerSec(0);
		this.setMaterial(material);
		this.setShape(new EmitterSphereShape(Vector3f.ZERO, 0.8f));
		this.setImagesX(2);
		this.setImagesY(2); // 2x2 texture animation
		this.setStartColor(new ColorRGBA(0.0f, 0.0f, 0.0f, 0.7f)); // dark
																	// gray
		// smoke.setEndColor(new ColorRGBA(0.5f, 0.5f, 0.5f,
		// 0.01f)); // gray
		this.setEndColor(new ColorRGBA(ColorRGBA.Brown.r, ColorRGBA.Brown.g, ColorRGBA.Brown.b, 0.01f));
		
		this.setStartSize(20);
		this.setEndSize(30);
		
		this.setGravity(0, -0.01f, 0);
		this.setLowLife(4f);
		this.setHighLife(6f);
	}



}
