import Graphics.Image;
import MyMath.OpenSimplexNoise.FastNoise;
import MyMath.OpenSimplexNoise.Noise;
import MyMath.Vector.Vector3;
import MyMath.Vector.Vector4;
import WorldObjects.LightSource;
import WorldObjects.Objects.Primitives.*;
import WorldObjects.Objects.WorldObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static MyMath.OpenSimplexNoise.SimplexNoise.noise;

public class Main {
 public static void main(String[] args) throws IOException
        {
            FastNoise fn = new FastNoise();
            fn.SetCellularDistanceFunction(FastNoise.CellularDistanceFunction.Euclidean);
            fn.SetCellularReturnType(FastNoise.CellularReturnType.Distance2Div);

            for (int i = 0 ; i < 360 ; ++ i)
            {
                Scene scene = setUpScene(fn, i/360.0*Math.PI);
                long start = System.currentTimeMillis();
                Image image = scene.renderScene(480, 480, new Vector3(0, 0, 0), 60, 30, 0.01, true);
                long end = System.currentTimeMillis();
                System.out.println("Frame took " + (end - start) + "ms");
                image.save("frame_" + String.format("%03d", i));
            }

        }


        //The noise functions are very expensive to compute
        public static double getNoise(Vector3 p, Double amplitude,Double freq)
        {
            Double sum = 0.0;
            for (int i = 0 ; i < 3 ; ++i)
            {
                sum += noise(p.getX()/freq*Math.pow(2,i),p.getY()/freq*Math.pow(2,i),p.getZ()/freq*Math.pow(2,i))*amplitude/Math.pow(2,i);
            }
            return sum;
        }

        public static double getNoise(Vector4 p, Double amplitude, Double freq)
        {
            Double sum = 0.0;
            for (int i = 0 ; i < 3 ; ++i)
            {
                sum += noise(p.getX()/freq*Math.pow(2,i),p.getY()/freq*Math.pow(2,i),p.getZ()/freq*Math.pow(2,i), p.getW()/freq*Math.pow(2,i))*amplitude/Math.pow(2,i);
            }
            return sum;
        }



        public static Scene setUpScene(FastNoise fn, double t)
        {


            List<WorldObject> shapes = new ArrayList<>();
            LightSource light = new LightSource(new Vector3(0,20,-30),10000000);
            //shapes.add(new Plane(new Vector3(0,100,0), new Vector3(0,1,0) ));
            //WorldObject o =new Sphere(new Vector3(0,0,-300), 100 );
            //WorldObject o1 = new DisplacedObject(o, (p) -> 0);
            //shapes.add(o1);
            //shapes.add(o3);
            //shapes.add(new CombinedObjects(o2, o3,0.2,false,false,false));
            //shapes.add(new Plane(new Vector3(-100,0,0), new Vector3(-1,0,0) ));
            //shapes.add(new Plane(new Vector3(100,0,0), new Vector3(1,0,0) ));
            //shapes.add(new Plane(new Vector3(0,0,-2000), new Vector3(0,0,1) ));//TOO DO DE PLANE IS BROKEN
            //shapes.add(new Plane(new Vector3(0,0,100), new Vector3(0,0,1) ));
            //WorldObject s0 = new Sphere(new Vector3(0,0,-500),40);
            //WorldObject o0 = new DisplacedObject(s0,(p) -> fn.GetCellular((float)p.getX()*8,(float)p.getY()*8,(float)p.getZ()*8)*5);

            WorldObject s1 = new Sphere(new Vector3(0,0,-60),20);
            WorldObject o1 = new DisplacedObject(s1,(p) -> getNoise(new Vector4(p.getX(),p.getY(),p.getZ(), t*30), 5.0, 20.0));
            //WorldObject o1 = new DisplacedObject(s1,(p) -> noise(p.getX()/7,p.getY()/7,p.getZ()/7,t)*5);

            //WorldObject c0 = new CombinedObjects(o0,o1,1,true,false,false);
            //WorldObject o1 = new Sphere(new Vector3(0,0,-100),12);
            //WorldObject o2 = new Sphere(new Vector3(0,16,-100),8);
            //WorldObject o3 = new CombinedObjects(o1,o2,1,false,false,false);
            //WorldObject o4 = new CombinedObjects(o0,o3,1,false,false,false);
            shapes.add(o1);
            Scene scene = new Scene(shapes, light);
            return scene;
        }






}
