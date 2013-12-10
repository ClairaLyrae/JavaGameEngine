#version 150

uniform sampler2D tex_diffuse;

uniform vec4 flare_color;

in vec2 texcoords;

out vec4 fragColor;

void main()
{
	fragColor = texture2D(tex_diffuse, texcoords);
}
