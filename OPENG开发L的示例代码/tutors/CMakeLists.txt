PROJECT( tutors )

SET( PROGRAMS fog lightmaterial lightposition projection shapes texture
transformation )

FIND_PACKAGE( GLUT )
FIND_PACKAGE( OPENGL )

FOREACH( program ${PROGRAMS} )
  ADD_EXECUTABLE( ${program} ${program}.c glm.c )
  TARGET_LINK_LIBRARIES( ${program} ${GLUT_LIBRARY} ${OPENGL_LIBRARY} )
ENDFOREACH( program )
