#include "render.h"

void Java_com_crackedcarrot_NativeRender_nativeDataPoolSize(JNIEnv* env,
															jobject thiz, 
															jint type, 
															jint size){

                                                                  
    noOfSprites[type] = size;
    if(noOfSprites[type] > 0 && (renderSprites[type] = malloc(sizeof(GLSprite) * noOfSprites[type])) == NULL){
        
        __android_log_print(ANDROID_LOG_ERROR, "NATIVE ALLOC","MALLOC ERROR in nativeDataPoolSize");
    }
    //__android_log_print(ANDROID_LOG_DEBUG, 
	//	    				"NATIVE ALLOC",
	//	    				"Allocating memory pool for Sprites, Type %d of size %d ", 
	//	    				type, noOfSprites[type]);
}

void Java_com_crackedcarrot_NativeRender_nativeAllocTextureBuffers(JNIEnv* env, jobject thiz, jint length){

    if(length > 0){
        
        if((texData = malloc(sizeof(textureData)*length)) == NULL){
            __android_log_print(ANDROID_LOG_ERROR, "NATIVE ALLOC","MALLOC ERROR in allocTextureBuffers");
        }
        texDataLength = length;
        //__android_log_print(ANDROID_LOG_DEBUG, "TEXTURE BUFFER ALLOC" , "Allocating buffer for max %d textures", length);
    }
    else{
        __android_log_print(ANDROID_LOG_ERROR, "TEXTURE BUFFER ALLOC" , "Invalid buffer length! Buffer not allocated!");
    }
}

void Java_com_crackedcarrot_NativeRender_nativeSetTextureBuffer(JNIEnv* env, jobject thiz, jobject textureData){
    jclass class = (*env)->GetObjectClass(env, textureData);

    jfieldID id = (*env)->GetFieldID(env, class, "texIndex", "I");
    jint index = (*env)->GetIntField(env, textureData, id);
    
    id = (*env)->GetFieldID(env, class, "mTextureName", "I");
    jint textureName = (*env)->GetIntField(env, textureData, id);
    
    texData[index].textureName = textureName;

    
    id = (*env)->GetFieldID(env, class, "nFrames", "I");
    jint nFrames = (*env)->GetIntField(env, textureData, id);
    
    texData[index].nFrames = nFrames;

    //Texture Coords
	GLsizeiptr textCoordBufSize;
	textCoordBufSize = sizeof(GLfloat) * 4 * 2;
    GLfloat textureCoordBuffer[4*2];
	
	if((texData[index].texCoBuffNames = malloc(nFrames * sizeof(GLuint))) == NULL){
	    __android_log_print(ANDROID_LOG_ERROR, "NATIVE ALLOC","MALLOC ERROR in setTextureBuffer");
	}
	glGenBuffers(nFrames, texData[index].texCoBuffNames);
	
	GLfloat texFraction = 1.0 / nFrames;
	GLfloat startFraction = 0.0;
	GLfloat endFraction;
	int i;
	for(i = 0; i < nFrames; i++){
		endFraction = startFraction + texFraction;
		/*__android_log_print(ANDROID_LOG_DEBUG, 
		                "HWBUFFER ALLOC", 
		                "Allocating texCoords Set No: %d from %f to %f", 
		                i ,startFraction, endFraction );*/
		textureCoordBuffer[0] = startFraction; 	textureCoordBuffer[1] = 1.0;
		textureCoordBuffer[2] = endFraction; 	textureCoordBuffer[3] = 1.0;
		textureCoordBuffer[4] = startFraction; 	textureCoordBuffer[5] = 0.0;
		textureCoordBuffer[6] = endFraction; 	textureCoordBuffer[7] = 0.0;
		glBindBuffer(GL_ARRAY_BUFFER, texData[index].texCoBuffNames[i]);
		glBufferData(GL_ARRAY_BUFFER, textCoordBufSize, textureCoordBuffer,GL_STATIC_DRAW);
		startFraction = endFraction;
	}
	
	glBindBuffer(GL_ARRAY_BUFFER, 0);
}
    
void Java_com_crackedcarrot_NativeRender_nativeAlloc(JNIEnv*  env, 
													 jobject thiz, 
													 jint spriteNO, 
													 jobject sprite){
													
	//Get the class and read the type info
	jclass class = (*env)->GetObjectClass(env, sprite);
	jfieldID id = (*env)->GetFieldID(env, class, "type", "I");
	jint type = (*env)->GetIntField(env, sprite, id);
	
	//Use type to figure out what element to manipulate
	GLSprite* thisSprite = &renderSprites[type][spriteNO];
	//Set a variable, dont cache reference as we do with the rest of the members of the
	//sprite class, since the type is imutable.
	thisSprite->type = type;
	
	id = (*env)->GetFieldID(env, class, "subType", "I");
	jint subType = (*env)->GetIntField(env, sprite, id);
	thisSprite->subType = subType;
	
	//Cache reference to this object
	thisSprite->object = (*env)->NewGlobalRef(env,sprite);
	
		//cache the x,y,z pos ID
	id = (*env)->GetFieldID(env, class, "x", "F");
	thisSprite->x = id;
	id = (*env)->GetFieldID(env, class, "y", "F");
	thisSprite->y = id;
	id = (*env)->GetFieldID(env, class, "z", "F");
	thisSprite->z = id;
	
		//cache the width/height IDs
	id = (*env)->GetFieldID(env, class, "width", "F");
	thisSprite->width = id;
	id = (*env)->GetFieldID(env, class, "height", "F");
	thisSprite->height = id;
	id = (*env)->GetFieldID(env, class, "scale", "F");
	thisSprite->scale = id;
	
	id = (*env)->GetFieldID(env, class, "draw", "Z");
	thisSprite->draw = id;
	
	id = (*env)->GetFieldID(env, class, "r", "F");
	thisSprite->r = id;
	id = (*env)->GetFieldID(env, class, "g", "F");
	thisSprite->g = id;
	id = (*env)->GetFieldID(env, class, "b", "F");
	thisSprite->b = id;
	id = (*env)->GetFieldID(env, class, "opacity", "F");
	thisSprite->opacity = id;

	id = (*env)->GetFieldID(env, class, "cFrame", "I");
	thisSprite->cFrame = id;
	
		//cache TextureName
	id = (*env)->GetFieldID(env, class, "texIndex", "I");
	thisSprite->textureIndex = id;
	
	//If this is not the first sprite of its type and its not an animation
	//We can just use the same VBOs as the last sprite.
    GLSprite* last = NULL;
	if(spriteNO != 0){
		last = &renderSprites[thisSprite->type][spriteNO-1];
	}
	if(last != NULL && last->subType == thisSprite->subType){
		thisSprite->bufferName = last->bufferName;
		thisSprite->indexCount = last->indexCount;
	}
	else{               
		if((thisSprite->bufferName = malloc(sizeof(GLuint)*2)) == NULL){
		    __android_log_print(ANDROID_LOG_ERROR, "NATIVE ALLOC","MALLOC ERROR in nativeAlloc");
		}
	
	    thisSprite->bufferName[0] = 0; 
	    thisSprite->bufferName[1] = 0;
	    
		initHwBuffers(env, thisSprite);
	}

    //Class specific code
	if(type == CREATURE){
	    jclass creature = (*env)->FindClass(env, "com/crackedcarrot/Creature");

	    if(creature == NULL){
            __android_log_print(ANDROID_LOG_ERROR, "NATIVE ALLOC", "Failed to get creature class");
	    }

	    if((*env)->IsInstanceOf(env, thisSprite->object, creature)){
            if((thisSprite->crExtens = malloc(sizeof(crExtensions))) == NULL){
                __android_log_print(ANDROID_LOG_ERROR, "NATIVE ALLOC","MALLOC ERROR in nativeAlloc");
            }
            thisSprite->crExtens->dead = (*env)->GetFieldID(env, creature, "dead", "Z");

	    }
	    else{
            __android_log_print(ANDROID_LOG_ERROR, "NATIVE ALLOC", "ERROR, java class and typeVar does not MATCH!!");
	    }
	}
	else{
        thisSprite->crExtens = NULL;
	}
}

void initHwBuffers(JNIEnv* env, GLSprite* sprite){
	GLsizeiptr vertBufSize;
	GLsizeiptr indexBufSize;
	
	vertBufSize = sizeof(GLfloat) * 4 * 3;
	indexBufSize = sizeof(GLushort) * 6;
	
	GLfloat vertBuffer[4*3];
	GLushort  indexBuffer[6];
	sprite->indexCount = 6;	
	
	//This be the vertex order for our quad, its totaly square man.
	indexBuffer[0] = 0;
	indexBuffer[1] = 1;
	indexBuffer[2] = 2;
	indexBuffer[3] = 1;
	indexBuffer[4] = 2;
	indexBuffer[5] = 3;
		
	GLfloat width = (*env)->GetFloatField(env, sprite->object, sprite->width);
	GLfloat height = (*env)->GetFloatField(env, sprite->object, sprite->height);

	//__android_log_print(ANDROID_LOG_DEBUG, "HWBUFFER ALLOC", "New vertBuffer with sizes width: %f, height: %f", width, height);
		
	//VERT1
	vertBuffer[0] = 0.0;
	vertBuffer[1] = 0.0;
	vertBuffer[2] = 0.0;	
	//VERT2
	vertBuffer[3] = width;
	vertBuffer[4] = 0.0;
	vertBuffer[5] = 0.0;
	//VERT3
	vertBuffer[6] = 0.0;
	vertBuffer[7] = height;
	vertBuffer[8] = 0.0;
	//VERT4
	vertBuffer[9] = width;
	vertBuffer[10] = height;
	vertBuffer[11] = 0.0;
	//WOOO I CAN HAS QUAD!
	
	glGenBuffers(2, sprite->bufferName);
	//__android_log_print(ANDROID_LOG_DEBUG, "HWBUFFER ALLOC", "GenBuffer retured error: %d", glGetError());
	glBindBuffer(GL_ARRAY_BUFFER, sprite->bufferName[VERT_OBJECT]);
	glBufferData(GL_ARRAY_BUFFER, vertBufSize, vertBuffer, GL_STATIC_DRAW);
	
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, sprite->bufferName[INDEX_OBJECT]);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBufSize, indexBuffer, GL_STATIC_DRAW);
	
	/*__android_log_print(ANDROID_LOG_DEBUG, 
		                "HWBUFFER ALLOC", 
		                "Sprite has been assigned the new buffers: %d, %d and %d", 
		                sprite->bufferName[INDEX_OBJECT] ,sprite->bufferName[VERT_OBJECT], sprite->texCoBuffNames[0] );*/

	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

}

void Java_com_crackedcarrot_NativeRender_nativeFreeSprites(JNIEnv* env, jobject thiz){
	GLSprite* currSprt;
	int spritesToFree;
	int i;
	int j;
	int k;
	
	for(j = 0; j < 6; j++){
		spritesToFree = noOfSprites[j];
		for(i = 0; i < spritesToFree; i++){
			currSprt = &renderSprites[j][i];
			//__android_log_print(ANDROID_LOG_DEBUG, "NATIVE_FREE_SPRITES", 
			//"Freeing sprite %d:%d of %d:%d", j,i,6,spritesToFree);
            //__android_log_print(ANDROID_LOG_DEBUG, "NATIVE_FREE_SPRITES", "Trying to free the Global Ref");
            (*env)->DeleteGlobalRef(env, currSprt->object);
            if(currSprt->crExtens != NULL){
                //__android_log_print(ANDROID_LOG_DEBUG, "NATIVE_FREE_SPRITES", "Trying to free the Creature Ext");
                free(currSprt->crExtens);
                currSprt->crExtens = NULL;
            }
                
            if(currSprt->bufferName != NULL){
                //__android_log_print(ANDROID_LOG_DEBUG, "NATIVE_FREE_SPRITES", "Deleteing GL buffers");
			    glDeleteBuffers(2, currSprt->bufferName);
				for(k = i+1; k < spritesToFree; k++){
				    //__android_log_print(ANDROID_LOG_DEBUG, "NATIVE_FREE_SPRITES", 
				    //"Adress0: %x Adress1: %x",currSprt->bufferName,renderSprites[j][k].bufferName);
				    if(currSprt->bufferName  == renderSprites[j][k].bufferName){
			            //__android_log_print(ANDROID_LOG_DEBUG, "NATIVE_FREE_SPRITES", 
			            //"Shared buffer, avoid double free");
			            renderSprites[j][k].bufferName = NULL;
			        }
			    }
			    //__android_log_print(ANDROID_LOG_DEBUG, "NATIVE_FREE_SPRITES", "All shared buffers clean, freeing mem");
				free(currSprt->bufferName);
				currSprt->bufferName = NULL;
			}
        }
    //__android_log_print(ANDROID_LOG_DEBUG, "NATIVE_FREE_SPRITES", "Freeing list: %d", j);
	free(renderSprites[j]);
	renderSprites[j] = NULL;
	noOfSprites[j] = 0;
    }
}

void Java_com_crackedcarrot_NativeRender_nativeFreeTex(JNIEnv* env, jobject thiz, jint index){
    glDeleteTextures(1, &texData[index].textureName);
    glDeleteBuffers(texData[index].nFrames, texData[index].texCoBuffNames);
    free(texData[index].texCoBuffNames);
    texData[index].texCoBuffNames = NULL;
}
