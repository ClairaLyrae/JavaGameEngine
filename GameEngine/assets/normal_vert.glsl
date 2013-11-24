#version 430

	attribute vec3 in_position;
	attribute vec3 in_normal;
	attribute vec2 in_texcoord;
	attribute vec4 in_tangent;
	attribute vec4 in_color;
	attribute float in_point_size;
	
	varying vec3 normal;
	varying vec3 tangent;
	varying vec3 binormal;
	
	varying vec3 vertex_light_half_vector;
	varying vec3 vertex_light_position;
	
	void main()
	{
	   vec3 c1 = cross(gl_Normal, vec3(0.0, 0.0, 1.0)); 
	   vec3 c2 = cross(gl_Normal, vec3(0.0, 1.0, 0.0)); 
	   if(length(c1)>length(c2))
	   {
	      tangent = c1;   
	   }
	   else
	   {
	      tangent = c2;   
	   }
	   tangent = normalize(tangent);
	
	    normal = normalize(gl_NormalMatrix * gl_Normal);
	    tangent = normalize(gl_NormalMatrix * tangent);
	    binormal = normalize(cross(normal, tangent));
	    
	    gl_TexCoord[0] = gl_MultiTexCoord0;
	    gl_Position = ftransform();
	    
	    vertex_light_half_vector = normalize(gl_LightSource[0].halfVector.xyz);
	    vertex_light_position = normalize(gl_LightSource[0].position.xyz);
	    
	    normal = gl_Normal;
	}