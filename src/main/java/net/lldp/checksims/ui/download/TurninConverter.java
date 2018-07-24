package net.lldp.checksims.ui.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

public class TurninConverter {
	public static void formatSubmission(File from, File to, String suffixes) throws Exception {
		if(!from.exists()) {
			throw new Exception("'" + from.getAbsolutePath() + "' does not exist.");
		}
		
		if(!to.exists()) {
			throw new Exception("'" + to.getAbsolutePath() + "' already exists.");
		}
		
		File code = new File(to.getPath() + "/code");
		code.mkdirs();
		
		String[] suffixList = suffixes.split(",");
		for(int i = 0; i < suffixList.length; i++) {
			suffixList[i] = suffixList[i].trim();
		}
		
		try {
			format(from, to, code, suffixList);
		} catch(Exception e) {
			throw e;
		}
	}
	
	public static void delete(File file) {
		if(file.isDirectory()) {
			for(File f : file.listFiles()) {
				delete(f);
			}
		}
		file.setWritable(true);
		file.delete();
	}
	
	private static void format(File start, File end, File code, String[] suffixes) throws Exception {
		if(start.isDirectory()) {
			for(File f : start.listFiles()) {
				try {
					format(f, end, code, suffixes);
				} catch(Exception e) {
					throw e;
				}
			}
			delete(start);
		} else {
			String name = start.getName();
			if(name.endsWith(".zip")) {
				try {
					String folderName = name.substring(0, (int) (name.length() - 4));
					File toFolder = new File(getUnusedName(start.getParentFile(), folderName));
					toFolder.mkdir();
					unzip(start, toFolder);
					delete(start);
					format(toFolder, end, code, suffixes);
				} catch(Exception e) {
					throw e;
				}
			} else if(name.endsWith(".tar")) {
				try {
					String folderName = name.substring(0, (int) (name.length() - 4));
					File toFolder = new File(getUnusedName(start.getParentFile(), folderName));
					toFolder.mkdir();
					untar(start, toFolder);
					delete(start);
					format(toFolder, end, code, suffixes);
				} catch(Exception e) {
					throw e;
				}
			} else if(name.endsWith(".gz")) {
				try {
					String fileName = name.substring(0, (int) (name.length() - 3));
					File toFile = new File(getUnusedName(start.getParentFile(), fileName));
					ungz(start, toFile);
					delete(start);
					format(toFile, end, code, suffixes);
				} catch(Exception e) {
					throw e;
				}
			} else {
				for(String s : suffixes) {
					if(name.endsWith(s)) {
						try {
							Path to = new File(getUnusedName(code, name)).toPath();
							Files.move(start.toPath(), to);
						} catch(Exception e) {
							throw e;
						}
						return;
					}
				}
				try {
					Path to = new File(getUnusedName(end, name)).toPath();
					Files.move(start.toPath(), to);
				} catch(Exception e) {
					throw e;
				}
			}
		}
	}
	
	public static String getUnusedName(File folder, String base) throws Exception {		
		String[] nameList;
		try {
			nameList = folder.list();
		} catch(Exception e) {
			throw e;
		}
		
		HashMap<String, Void> names = new HashMap<String, Void>();
		for(String n : nameList) {
			names.put(n, null);
		}		
		long i = 0;
		String name;
		int index;
		if(!names.containsKey(base)) {
			return folder.getPath() + "/" + base;
		}
		while(true) {
			index = base.lastIndexOf('.');
			if(index < 1) {
				name = base + i;
			} else {
				name = base.substring(0, index) + i + base.substring(index);
			}
			if(!names.containsKey(name)) {
				return folder.getPath() + "/" + name;
			}
			i++;
		}
	}
	
	private static void unzip(File zip, File toFolder) throws Exception {
		ZipInputStream zipInputStream = null;
		try {
			zipInputStream = new ZipInputStream(new FileInputStream(zip.getPath()));
			ZipEntry zipEntry = null;
			byte[] buf = new byte[1024];
			while((zipEntry = zipInputStream.getNextEntry()) != null) {
				File file = new File(toFolder.getPath() + "/" + zipEntry.getName());
				if(file.exists()) {
					throw new Exception("'" + file.getAbsolutePath() + "' already exsists.");
				}
				if(zipEntry.isDirectory()) {
					file.mkdirs();
				} else {
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					int read;
					while((read = zipInputStream.read(buf)) > 0) {
						fileOutputStream.write(buf, 0, read);
					}
					fileOutputStream.close();
				}
			}
		} catch(Exception e) {
			if(zipInputStream != null) {
				zipInputStream.closeEntry();
				zipInputStream.close();
			}
			throw e;
		}
		zipInputStream.closeEntry();
		zipInputStream.close();
	}
	
	private static void untar(File tar, File toFolder) throws Exception {
		TarArchiveInputStream tais = null;
		try {
			tais = new TarArchiveInputStream(new FileInputStream(tar));
			TarArchiveEntry tarArchiveEntry = null;
			byte[] buf = new byte[1024];
			while((tarArchiveEntry = tais.getNextTarEntry()) != null) {
				File file = new File(toFolder.getPath() + "/" + tarArchiveEntry.getName());
				if(file.exists()) {
					throw new Exception("'" + file.getAbsolutePath() + "' already exists.");
				}
				if(tarArchiveEntry.isDirectory()) {
					file.mkdirs();
				} else {
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					int read;
					while((read = tais.read(buf)) > 0) {
						fileOutputStream.write(buf, 0, read);
					}
					fileOutputStream.close();
				}
			}
		} catch(Exception e) {
			if(tais != null) {
				tais.close();
			}
		}
		tais.close();
	}
	
	private static void ungz(File gz, File toFile) throws Exception {
		if(toFile.exists()) {
			throw new Exception("'" + toFile.getAbsolutePath() + "' already exists.");
		}
		GZIPInputStream gzipInputStream = null;
		try {
			gzipInputStream = new GZIPInputStream(new FileInputStream(gz));
			FileOutputStream fileOutputStream = new FileOutputStream(toFile);
			byte[] buf = new byte[1024];
			
			int read;
			while((read = gzipInputStream.read(buf)) > 0) {
				fileOutputStream.write(buf, 0, read);
			}
			fileOutputStream.close();
		} catch(Exception e) {
			if(gzipInputStream != null) {
				gzipInputStream.close();
			}
		}		
		gzipInputStream.close();
	}
}

