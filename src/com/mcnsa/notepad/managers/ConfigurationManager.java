package com.mcnsa.notepad.managers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.bukkit.configuration.file.FileConfiguration;

import com.mcnsa.notepad.annotations.CustomString;
import com.mcnsa.notepad.annotations.Setting;
import com.mcnsa.notepad.exceptions.SettingException;
import com.mcnsa.notepad.utilities.Logger;

public class ConfigurationManager {
	// store our config
	private FileConfiguration fileConfiguation = null;
	
	public ConfigurationManager(FileConfiguration fileConfiguration) {
		// store our config
		this.fileConfiguation = fileConfiguration;
		
		// deal with defaults
		fileConfiguration.options().copyDefaults(true);
		
		// load our data
		load();
	}
	
	public void load() {
		// get all our loaded classes
		Vector<Class<?>> classes = null;
		try {
			classes = getLoadedClasses();
		}
		catch(Exception e) {
			Logger.error("Unhandled exception (%s): %s!", e.getClass().getName(), e.getMessage());
			e.printStackTrace();
		}
		
		// copy the data
		List<Class<?>> classList = null;
		synchronized(classes) {
			classList = new ArrayList<Class<?>>(classes);
		}
		
		// go through all classes and find @Setting annotations
		for(Class<?> clazz: classList) {
			// loop over all the fields in the component
			for(Field field: clazz.getFields()) {		
				try {		
					// get the annotation node
					String node = null;
					
					// get the field type
					Class<?> type = field.getType();
					
					// make sure both annotations aren't there
					if(field.isAnnotationPresent(Setting.class) && field.isAnnotationPresent(CustomString.class)) {
						throw new SettingException("Field '%s' can't have both @Setting and @CustomString annotations!", field.getName());
					}
					else if(field.isAnnotationPresent(Setting.class)) {
						Setting setting = field.getAnnotation(Setting.class);
						node = setting.node();
						//Logger.debug("Setting '%s' for node '%s'", field.getName(), node);
					}
					else if(field.isAnnotationPresent(CustomString.class)) {
						// make sure it's a string
						if(!type.equals(String.class)) {
							throw new SettingException("Field '%s' must be a String if it has the @CustomString annotation!", field.getName());
						}

						CustomString customString = field.getAnnotation(CustomString.class);
						node = customString.node() + "-string";
						//Logger.debug("Custom string '%s' for node '%s'", field.getName(), node);
					}
					else {
						// not annotated
						continue;
					}
					
				
					// now determine what to do based on what type of variable it is
					if(type.equals(String.class)) {
						// single string
						this.fileConfiguation.addDefault(node, (String)field.get(null));
						field.set(null, this.fileConfiguation.getString(node, (String)field.get(null)));
					}
					else if(type.equals(String[].class)) {
						// list of strings
						this.fileConfiguation.addDefault(node, (String[])field.get(null));
						List<String> result = this.fileConfiguation.getStringList(node);
						field.set(null, result.toArray(new String[result.size()]));
					}
					else if(type.equals(int.class)) {
						// single int
						this.fileConfiguation.addDefault(node, field.getInt(null));
						field.setInt(null, this.fileConfiguation.getInt(node, field.getInt(null)));
					}
					else if(type.equals(Integer[].class)) {
						// list of ints
						this.fileConfiguation.addDefault(node, (Integer[])field.get(null));
						List<Integer> result = this.fileConfiguation.getIntegerList(node);
						field.set(null, result.toArray(new Integer[result.size()]));
					}
					else if(type.equals(boolean.class)) {
						// single boolean
						this.fileConfiguation.addDefault(node, field.getBoolean(null));
						field.setBoolean(null, this.fileConfiguation.getBoolean(node, field.getBoolean(null)));
					}
					else if(type.equals(Boolean[].class)) {
						// list of booleans
						this.fileConfiguation.addDefault(node, (Boolean[])field.get(null));
						List<Boolean> result = this.fileConfiguation.getBooleanList(node);
						field.set(null, result.toArray(new Boolean[result.size()]));
					}
					else if(type.equals(float.class)) {
						// single float
						this.fileConfiguation.addDefault(node, field.getFloat(null));
						field.setFloat(null, (float)this.fileConfiguation.getDouble(node, field.getFloat(null)));
					}
					else if(type.equals(Float[].class)) {
						// list of floats
						this.fileConfiguation.addDefault(node, (Float[])field.get(null));
						List<Float> result = this.fileConfiguation.getFloatList(node);
						field.set(null, result.toArray(new Float[result.size()]));
					}
					else if(type.equals(long.class)) {
						// single boolean
						this.fileConfiguation.addDefault(node, field.getLong(null));
						field.setLong(null, this.fileConfiguation.getLong(node, field.getLong(null)));
					}
					else if(type.equals(Long[].class)) {
						// list of booleans
						this.fileConfiguation.addDefault(node, (Long[])field.get(null));
						List<Long> result = this.fileConfiguation.getLongList(node);
						field.set(null, result.toArray(new Long[result.size()]));
					}
					else {
						throw new SettingException("Unrecognized setting type '%s' for field %s.%s!", type.toString(), clazz.getSimpleName(), field.getName());
					}
				}
				catch(Exception e) {
					Logger.warning(e.getMessage() + " &c(ignoring setting)");
				}
			}
		}
	}
	
	private static Vector<Class<?>> getLoadedClasses() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		// get a list of all our classes
		//Logger.debug("Getting classes...");
		Field f = ClassLoader.class.getDeclaredField("classes");
		//Logger.debug("Type: %s", f.getType().getName());
		f.setAccessible(true);
		@SuppressWarnings("unchecked")
		Vector<Class<?>> classes = (Vector<Class<?>>)f.get(ConfigurationManager.class.getClassLoader());
		/*for(Class<?> clazz: classes) {
			Logger.debug("Found class '%s' (%s)!", clazz.getName(), clazz.getPackage().getName());
		}
		Logger.debug("done!");*/
		
		return classes;
	}
}
