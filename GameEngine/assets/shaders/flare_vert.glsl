#version 150

uniform mat4 p;
uniform mat4 v;
uniform mat4 mv;

uniform float flare_size;
uniform int flare_always_on_top;
uniform int flare_allow_perspective;
uniform int flare_draw_at_infinity;
uniform int flare_rotate_to_up;

in vec3 in_position;

out vec2 texcoords;

void main()
{
	mat4 mvp = mv * p;
	
	//screen-aligned axes
	vec3 axis1 = vec3(	mvp[0][0], 
						mvp[1][0],
						mvp[2][0]);
						
	vec3 axis2 = vec3(	mvp[0][1], 
						mvp[1][1],
						mvp[2][1]);


	//offset from center point
	vec3 corner = (axis1 + axis2)*flare_size + in_position;
	
	// position in eye space
	gl_Position = vec4(corner, 1) * mvp;

}
	//texcoords = in_position.xy;
	//gl_Position = (vec4(0.0, 0.0, 0.0, (5 + (1/flare_size))/gl_Position.z)*mv + vec4(in_position.x, in_position.y, 0.0, 0.0))*p;
	//gl_Position.x -= 0.75;
	//gl_Position.y -= 1.25;
	//if(flare_always_on_top == 0)
	//{
	//	gl_Position.z = -0.99;
	//}