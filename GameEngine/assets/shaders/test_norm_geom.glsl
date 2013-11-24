#version 330
layout(triangles) in;

layout(line_strip, max_vertices=21) out;

uniform mat4 mv;
uniform mat4 p;

in vec3[3] normal;
in vec3[3] tangent;
in vec3[3] bitangent;
out vec4 color;

void main()
{
	mat3 normalmatrix = transpose( inverse( mat3( transpose( mv ))));
	
	int i;
	for(i=0; i<3; i++)
	{
		vec4 P = gl_in[i].gl_Position * mv;
		vec4 N = normalize(vec4(normalmatrix * normal[i], 1));
		vec4 T = normalize(vec4(normalmatrix * tangent[i], 1));
		vec4 B = normalize(vec4(normalmatrix * bitangent[i], 1));
    
    	color = vec4(1, 0, 0, 1);
		gl_Position = P * p;
		EmitVertex();
		gl_Position = (P + N) * p;
		EmitVertex();
		EndPrimitive();  
		  
    	color = vec4(0, 1, 0, 1);
		gl_Position = P * p;
		EmitVertex();
		gl_Position = (P + T) * p;
		EmitVertex();
		EndPrimitive();
		
    	color = vec4(0, 0, 1, 1);
		gl_Position = P * p;
		EmitVertex();
		gl_Position = (P + B) * p;
		EmitVertex();
		EndPrimitive();
		
		
		
    
	}
	for(i=0; i<3; i++)
	{
		vec4 P2 = gl_in[i].gl_Position;
    
    	color = vec4(normalize((normalmatrix * normal[i])) , 1);
		gl_Position = P2 * mv * p;
		EmitVertex();
	}
	EndPrimitive();
}