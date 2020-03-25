package fr.geolabs.dev.mapmint4me;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.google.android.filament.Material;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.collision.Sphere;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class ARPage extends FragmentActivity {

    private ArFragment arFragment;


    private enum ShapeType
    {
        CUBE,
        CYLINDER,
        SPHERE

    }
    private ShapeType shapeType = ShapeType.CUBE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ar_page);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        Button cube = findViewById(R.id.cube);
        Button sphere = findViewById(R.id.sphere);
        Button cylinder = findViewById(R.id.cylinder);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

            if(shapeType == ShapeType.CUBE)
                placeCube(hitResult.createAnchor());
            else if(shapeType == ShapeType.SPHERE)
                placeSphere(hitResult.createAnchor());
            else
                placeCylinder(hitResult.createAnchor());

        });

        cube.setOnClickListener(view -> shapeType = ShapeType.CUBE );
        sphere.setOnClickListener(view -> shapeType = ShapeType.SPHERE);
        cylinder.setOnClickListener(view -> shapeType = ShapeType.CYLINDER);

    }

    private void placeCylinder(Anchor anchor) {
        MaterialFactory
                .makeOpaqueWithColor(this, new Color(android.graphics.Color.BLACK))
                .thenAccept(material -> {
                    ModelRenderable modelRenderable = ShapeFactory.makeCylinder(0.1f,0.2f,new Vector3(0f,0.1f,0f),material);
                    placeModel(modelRenderable,anchor);
                });

    }

    private void placeCube(Anchor anchor) {
        MaterialFactory
                .makeOpaqueWithColor(this, new Color(android.graphics.Color.BLUE))
                .thenAccept(material -> {
                    ModelRenderable modelRenderable =  ShapeFactory.makeCube(new Vector3(0.1f,0.1f,0.1f),new Vector3(0f,0.1f,0f),material);
                    placeModel(modelRenderable,anchor);
                });
    }


    private void placeSphere(Anchor anchor) {
        MaterialFactory
                .makeOpaqueWithColor(this,new Color(android.graphics.Color.RED))
                .thenAccept(material -> {
                    ModelRenderable modelRenderable = ShapeFactory.makeSphere(0.1f,new Vector3(0f,0.1f,0f),material);
                    placeModel(modelRenderable,anchor);
                });

    }

    private void placeModel(ModelRenderable modelRenderable, Anchor anchor) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
    }
}

