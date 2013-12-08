#version 150

uniform mat4 p;
uniform mat4 v;

in vec3 in_position;

out vec3 texcoords;

void main () 
{
	mat4 v_mod = v;
	v_mod[0].w = 0;
	v_mod[1].w = 0;
	v_mod[2].w = 0;
	texcoords = in_position;
	gl_Position = vec4(in_position, 1.0) * v_mod * p;
}