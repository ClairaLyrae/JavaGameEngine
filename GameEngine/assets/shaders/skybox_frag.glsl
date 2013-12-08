#version 150

uniform samplerCube tex_cube;

in vec3 texcoords;

out vec4 fragColor;

void main () 
{
	fragColor = texture(tex_cube, texcoords);
}