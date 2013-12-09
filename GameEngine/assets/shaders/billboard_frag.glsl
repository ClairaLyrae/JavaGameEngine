#version 330

uniform sampler2D tex_diffuse; 

in vec2 texcoord;
in vec4 color;

out vec4 fragColor;

void main()
{
	fragColor = texture2D(tex_diffuse, texcoord);
}