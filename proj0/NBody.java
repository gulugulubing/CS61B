public class NBody{
	public static double readRadius(String text){
		In in = new In(text);
		int num = in.readInt();
		double radius = in.readDouble();
		return radius;
	}
	
	public static Planet[] readPlanets(String text){
		int i=0;
		In in = new In(text);
		int num = in.readInt();
		Planet[] p = new Planet[num];
		double radius = in.readDouble();
		while(i<num){
			p[i]=new Planet(in.readDouble(),in.readDouble(),in.readDouble(),in.readDouble(),in.readDouble(),in.readString());
			i++;
		}
		return p;
	}

	public static void main(String[] args){
		double T,dt,radius;
		String filename;
		double time=0;
		double[] xForcesArray;
		double[] yForcesArray;

		T = Double.parseDouble(args[0]);
		dt = Double.parseDouble(args[1]);
		filename = args[2];
		Planet[] p = readPlanets(filename);
		radius = readRadius(filename);
		
		StdDraw.setScale(-radius, radius);
		StdDraw.clear();
		StdDraw.picture(0, 0, "images/starfield.jpg");
		StdDraw.show();

		for(Planet s:p){
			s.draw();
		}

	    StdDraw.enableDoubleBuffering();
		
		xForcesArray = new double[p.length];
		yForcesArray = new double[p.length];

		while(time<T){
			for(int i=0;i<p.length;i++){
				xForcesArray[i] = p[i].calcNetForceExertedByX(p);
				yForcesArray[i] = p[i].calcNetForceExertedByY(p);
			}
			for(int i=0;i<p.length;i++){
				p[i].update(dt,xForcesArray[i],yForcesArray[i]);
			}

			StdDraw.setScale(-radius, radius);
			StdDraw.clear();
			StdDraw.picture(0, 0, "images/starfield.jpg");
			StdDraw.show();

			for(Planet s:p){
				s.draw();
			}

			StdDraw.pause(10);
			time += dt;

		}

		StdOut.printf("%d\n", p.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < p.length; i++) {
    		StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  p[i].xxPos, p[i].yyPos, p[i].xxVel,
                  p[i].yyVel, p[i].mass, p[i].imgFileName);   
		}

	}
}
