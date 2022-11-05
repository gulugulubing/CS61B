public class Planet{
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;
	private static final double G =  6.67e-11;

	public Planet(double xP, double yP, double xV,
              double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;

	}

	public Planet(Planet p){
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;
	}

	public double calcDistance(Planet p){
		return Math.sqrt((this.xxPos-p.xxPos)*(this.xxPos-p.xxPos)+(this.yyPos-p.yyPos)*(this.yyPos-p.yyPos));
	}

	public double calcForceExertedBy(Planet p){
		return G*this.mass*p.mass/(this.calcDistance(p)*this.calcDistance(p));
	}

	public double calcForceExertedByX(Planet p){
		return this.calcForceExertedBy(p)*(p.xxPos-this.xxPos)/this.calcDistance(p);
	}

	public double calcForceExertedByY(Planet p){
		return this.calcForceExertedBy(p)*(p.yyPos-this.yyPos)/this.calcDistance(p);
	}

	public double calcNetForceExertedByX(Planet[] allp){
		double f=0;
		for(Planet p:allp){
			if(this.equals(p)){
				continue;
			}
			f = f + this.calcForceExertedByX(p);
		}
		return f;
	}

	public double calcNetForceExertedByY(Planet[] allp){
		double f=0;
		for(Planet p:allp){
			if(this.equals(p)){
				continue;
			}
			f = f + this.calcForceExertedByY(p);
		}
		return f;
	}
	
	public void update(double dt,double fX,double fY){
		xxVel = xxVel + fX*dt/mass;
		yyVel = yyVel + fY*dt/mass;
		xxPos = xxPos + xxVel*dt;
		yyPos = yyPos + yyVel*dt;
	}

	public void draw(){
		StdDraw.picture(xxPos, yyPos, "images/"+imgFileName);
		StdDraw.show();
	}
}