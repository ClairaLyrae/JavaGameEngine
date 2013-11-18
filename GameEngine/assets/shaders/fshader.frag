#version 150 core

uniform sampler2D texture_diff;
uniform sampler2D texture_norm;

in vec4 pass_Color;

void main(void) {
	// out_Color = pass_Color;
	// Override out_Color with our texture pixel
	gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0); //texture2D(texture_diff, gl_TexCoord[0].st);
}