#version 330

layout(triangles) in;

layout(triangle_strip, max_vertices=12) out;

uniform mat4 mv;
uniform mat4 p;

out vec2 texcoord;
out vec4 color;

void main()
{
	for(int i = 0; i < 3; i++)
	{	
    	vec4 center = gl_in[i].gl_Position * mv;
 
 
 		color = vec4(1, 1, 0, 1);
   	 	texcoord = vec2( 0, 1 );
    	gl_Position = (center + vec4(-1, 1, 0, 0)) * p;
    	EmitVertex();
 
    	
 		color = vec4(0, 1, 1, 1);
    	texcoord = vec2( 0, 0 );
    	gl_Position = (center + vec4(-1, -1, 0, 0)) * p;
    	EmitVertex();

 		color = vec4(1, 0, 1, 1);
    	texcoord = vec2( 1, 1 );
    	gl_Position = (center + vec4(1, 1, 0, 0)) * p;
    	EmitVertex();
    	
 		color = vec4(1, 0, 0, 1);
    	texcoord = vec2( 1, 0 );
    	gl_Position = (center + vec4(1, -1, 0, 0)) * p;
    	EmitVertex();
    	
    	EndPrimitive();
	}
	
}