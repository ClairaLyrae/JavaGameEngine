#version 150

uniform sampler2D tex_diffuse;

in vec2 texcoord;

out vec4 fragColor;

void main(void) 
{
	vec4 diffuse = texture2D(tex_diffuse, texcoord);
    fragColor = diffuse;
}