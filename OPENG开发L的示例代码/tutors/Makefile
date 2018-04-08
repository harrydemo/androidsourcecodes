###############################################################################
#
# Makefile
#
###############################################################################

HEADERS  = glm.h materials.h
SOURCES  = fog.c glm.c lightmaterial.c lightposition.c projection.c shapes.c \
           texture.c transformation.c
DEPENDS  = $(SOURCES:.c=.d)
OBJECTS  = $(SOURCES:.c=.o)
PROGRAMS = fog lightmaterial lightposition projection shapes texture \
           transformation

###############################################################################

ifdef DEBUG
OPTFLAGS = -g
else
OPTFLAGS = -O3
endif

CC      = gcc
CFLAGS  = -Wall $(OPTFLAGS)
LDFLAGS = -lGL -lglu -lglut

UNAME = $(shell uname)
ifeq ($(UNAME), Darwin)  # Mac OS X
# OpenGL and GLUT are frameworks, override LDFLAGS above
LDFLAGS = -framework OpenGL -framework GLUT
# Universal binary support
CFLAGS  += -arch ppc -arch i386
endif
ifeq ($(UNAME), CYGWIN_NT-5.1)  # Cygwin
# OpenGL and GLUT libs are named differently, override LDFLAGS above
LDFLAGS = -lopengl32 -lglu32 -lglut32
EXE = .exe
endif

###############################################################################

all: $(PROGRAMS)

$(PROGRAMS):
	$(CC) $(CFLAGS) $^ $(LDFLAGS) -o $@

define PROGRAM_template
$(1): $(addsuffix .o,$(1)) glm.o
endef
$(foreach t,$(PROGRAMS),$(eval $(call PROGRAM_template,$(t))))

clean:
	$(RM) $(OBJECTS) $(DEPENDS)
	$(RM) $(PROGRAMS:=$(EXE))

.PHONY: all clean

###############################################################################

%.o: %.c
	$(CC) -c $(CFLAGS) $< -o $@

%.d: %.c
	$(CC) -MM $< -o $@

###############################################################################

ifneq ($(MAKECMDGOALS),clean)
-include $(DEPENDS)
endif

###############################################################################
