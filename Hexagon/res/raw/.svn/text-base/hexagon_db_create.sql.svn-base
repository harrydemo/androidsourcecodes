--- create worldmap table ---
CREATE TABLE worldmap (map_id INTEGER PRIMARY KEY AUTOINCREMENT, map_name TEXT NOT NULL, map_desc TEXT NULL, map_width INTEGER, map_height INTEGER);
--- create mapcell table ---
CREATE TABLE mapcell (cell_id INTEGER PRIMARY KEY AUTOINCREMENT, cell_name TEXT NOT NULL, cell_desc TEXT NULL, cell_parent_map INTEGER, cell_img INTEGER, cell_map_row INTEGER, cell_map_column INTEGER);
--- create imagecrop table ---
CREATE TABLE imagecrop (img_id INTEGER PRIMARY KEY AUTOINCREMENT, img_name TEXT NOT NULL, img_desc TEXT NULL, img_parent_gal INTEGER, img_row INTEGER, img_column INTEGER, img_width INTEGER, img_height INTEGER);
--- create imagegallery table ---
CREATE TABLE imagegallery (gal_id INTEGER PRIMARY KEY AUTOINCREMENT, gal_name TEXT NOT NULL, gal_desc TEXT NULL, gal_width INTEGER, gal_height INTEGER, gal_img INTEGER);