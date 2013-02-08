package jadex.agentkeeper.view.effects;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class ShockwaveEffect extends ParticleEmitter
{

	public ShockwaveEffect(AssetManager assetManager)
	{
		super("Shockwave", Type.Triangle, 2);
		// TODO Auto-generated constructor stub
		
		this.setFaceNormal(Vector3f.UNIT_Y);
		this.setStartColor(new ColorRGBA(.48f, 0.17f, 0.01f, (float)(.8f / 1)));
		this.setEndColor(new ColorRGBA(.48f, 0.17f, 0.01f, 0f));
		
		this.setStartSize(0);
		this.setEndSize(10);
		this.setParticlesPerSec(0);
		this.setGravity(0, 0, 0);
		this.setLowLife(0.7f);
		this.setHighLife(0.7f);
		this.setImagesX(1);
		this.setImagesY(1);
		Material mata = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mata.setTexture("Texture", assetManager.loadTexture("textures/effects/explosion/shockwave.png"));
		this.setMaterial(mata);
	}



}
