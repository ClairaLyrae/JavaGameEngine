#version 150
#define MAX_LIGHTS 10

uniform mat4 m;
uniform mat4 v;
uniform mat4 mv;
uniform mat4 p;

in vec3 in_position;
in vec2 in_texcoord;

out vec2 texcoord;

void main(void) 
{
	texcoord = in_texcoord;
	
	gl_Position = vec4(in_position, 1) * mv * p;
}