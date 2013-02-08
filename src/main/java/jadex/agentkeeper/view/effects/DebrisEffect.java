package jadex.agentkeeper.view.effects;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class DebrisEffect extends ParticleEmitter
{

	public DebrisEffect(AssetManager assetManager)
	{
		super("Debris", Type.Triangle, 15);
		// TODO Auto-generated constructor stub
		

		this.setSelectRandomImage(true);
		this.setRandomAngle(true);
		this.setRotateSpeed(FastMath.TWO_PI * 2);
		this.setStartColor(new ColorRGBA(ColorRGBA.Brown.r, ColorRGBA.Brown.g, ColorRGBA.Brown.b, (float)(1.0f)));
		// debris.setEndColor(new ColorRGBA(.5f, 0.5f, 0.5f,
		// 0f));

		this.setEndColor(new ColorRGBA(ColorRGBA.Brown.r, ColorRGBA.Brown.g, ColorRGBA.Brown.b, 1.0f));

		this.setStartSize(2.5f);
		this.setEndSize(2.5f);
		
		this.setFacingVelocity(false);
		
		this.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 5, 0));
		this.getParticleInfluencer().setVelocityVariation(1f);

		this.setParticlesPerSec(0);
		this.setShape(new EmitterSphereShape(Vector3f.ZERO, 0.5f));
		// debris.setParticlesPerSec(0);
		this.setGravity(0, 4f, 0);
		this.setLowLife(0.7f);
		this.setHighLife(0.9f);
		this.setImagesX(3);
		this.setImagesY(3);
//		debris.setLocalTranslation(0, 1f, 0);
		Material matx = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		matx.setTexture("Texture", assetManager.loadTexture("textures/effects/explosion/Debris.png"));
		this.setMaterial(matx);
	}



}
