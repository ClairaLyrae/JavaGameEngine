#version 150
#define MAX_LIGHTS 10

uniform mat4 v;

uniform sampler2D tex_emissive;

in vec2 texcoord;

out vec4 fragColor;

void main(void) 
{
    vec4 tex = texture2D(tex_emissive, texcoord);
    fragColor.a = tex.r + tex.g + tex.b;
    fragColor.rgb = tex.rgb;
}
