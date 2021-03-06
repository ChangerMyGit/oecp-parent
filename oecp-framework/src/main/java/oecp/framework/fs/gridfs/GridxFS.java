/**
 * Copyright (c) www.bugull.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package oecp.framework.fs.gridfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import oecp.framework.mongo.mapper.MapperUtil;
import oecp.framework.mongo.mapper.Operator;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.document.mongodb.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

/**
 *
 * @author yongtree
 */
public class GridxFS {
    
    private final static Logger logger = Logger.getLogger(GridxFS.class);
    
    private GridFS fs;
    private DBCollection files;
    
    private final static String FS_FILES = "fs.files";
    
    public final static String FOLDER = "folder";
    public final static String FILENAME = "filename";

    private MongoTemplate mongoTemplate;
    
    @PostConstruct
    public void init(){
        DB db = mongoTemplate.getDb();
        fs = new GridFS(db);
        files = db.getCollection(FS_FILES);
    }
    
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
   
    public GridFSInputFile save(File file){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("originalName", file.getName());
        return save(file, UUID.randomUUID().toString(), null, map);
    }
    
    public GridFSInputFile save(File file, String filename){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("originalName", filename);
        return save(file, UUID.randomUUID().toString(), null, map);
    }
    
    public GridFSInputFile save(File file, String filename, String folderName){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("originalName", filename);
        return save(file, UUID.randomUUID().toString(), folderName, map);
    }
    
    public GridFSInputFile save(File file, String filename, String folderName, Map<String, Object> params){
        GridFSInputFile f = null;
        FileInputStream fileInputStream = null;
        try{
        	fileInputStream = new FileInputStream(file);
            f = fs.createFile(fileInputStream,file.getName());
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        f.setFilename(filename);
        setParams(f, folderName, params);
        f.save();
     	if(fileInputStream != null){//关闭文件流，防止原文件无法删除
    		try {
				fileInputStream.close();
			} catch (IOException e) {}
    	}
        return f;
    }
    
    public GridFSInputFile save(InputStream is, String filename){
        return save(is, filename, null, null);
    }
    
    public GridFSInputFile save(InputStream is, String filename, String folderName){
        return save(is, filename, folderName, null);
    }
    
    public  GridFSInputFile save(InputStream is, String filename, String folderName, Map<String, Object> params){
        GridFSInputFile f = fs.createFile(is);
        f.setFilename(filename);
        setParams(f, folderName, params);
        f.save();
        return f;
    }
    
    public GridFSInputFile save(byte[] data, String filename){
        return save(data, filename, null, null);
    }
    
    public GridFSInputFile save(byte[] data, String filename, String folderName){
        return save(data, filename, folderName, null);
    }
    
    public GridFSInputFile save(byte[] data, String filename, String folderName, Map<String, Object> params){
        GridFSInputFile f = fs.createFile(data);
        f.setFilename(filename);
        setParams(f, folderName, params);
        f.save();
        return f;
    }
    
    private void setParams(GridFSInputFile f, String folderName, Map<String, Object> params){
        if(folderName != null){
            if(params == null){
                params = new HashMap<String, Object>();
            }
            params.put(FOLDER, folderName);
        }
        if(params != null){
            Set<String> keys = params.keySet();
            for(String key : keys){
                f.put(key, params.get(key));
            }
        }
    }
    
    public GridFSDBFile findOne(String filename){
        return fs.findOne(filename);
    }
    
    public GridFSDBFile findOne(DBObject query){
        return fs.findOne(query);
    }
    
    public List<GridFSDBFile> find(DBObject query){
        return fs.find(query);
    }
    
    public List<GridFSDBFile> find(DBObject query, int pageNum, int pageSize){
        DBCursor cursor = files.find(query).skip((pageNum-1)*pageSize).limit(pageSize);
        return toFileList(cursor);
    }
    
    public List<GridFSDBFile> find(DBObject query, String orderBy){
        return find(query, MapperUtil.getSort(orderBy));
    }
    
    public List<GridFSDBFile> find(DBObject query, DBObject orderBy){
        DBCursor cursor = files.find(query).sort(orderBy);
        return toFileList(cursor);
    }
    
    public List<GridFSDBFile> find(DBObject query, String orderBy, int pageNum, int pageSize){
        return find(query, MapperUtil.getSort(orderBy), pageNum, pageSize);
    }
    
    public List<GridFSDBFile> find(DBObject query, DBObject orderBy, int pageNum, int pageSize){
        DBCursor cursor = files.find(query).sort(orderBy).skip((pageNum-1)*pageSize).limit(pageSize);
        return toFileList(cursor);
    }
    
    public List<GridFSDBFile> findByFolder(String folderName){
        DBObject query = new BasicDBObject(FOLDER, folderName);
        return find(query);
    }
    
    public List<GridFSDBFile> findByFolder(String folderName, int pageNum, int pageSize){
        DBObject query = new BasicDBObject(FOLDER, folderName);
        return find(query, pageNum, pageSize);
    }
    
    public List<GridFSDBFile> findByFolder(String folderName, DBObject orderBy){
        DBObject query = new BasicDBObject(FOLDER, folderName);
        return find(query, orderBy);
    }
    
    public List<GridFSDBFile> findByFolder(String folderName, String orderBy){
        return findByFolder(folderName, MapperUtil.getSort(orderBy));
    }
    
    public List<GridFSDBFile> findByFolder(String folderName, String orderBy, int pageNum, int pageSize){
        return findByFolder(folderName, MapperUtil.getSort(orderBy), pageNum, pageSize);
    }
    
    public List<GridFSDBFile> findByFolder(String folderName, DBObject orderBy, int pageNum, int pageSize){
        DBObject query = new BasicDBObject(FOLDER, folderName);
        return find(query, orderBy, pageNum, pageSize);
    }
    
    public List findAllFolder(){
        return files.distinct(FOLDER);
    }
    
    public void rename(String oldName, String newName){
        DBObject query = new BasicDBObject(FILENAME, oldName);
        DBObject dbo = files.findOne(query);
        dbo.put(FILENAME, newName);
        files.save(dbo);
    }
    
    public void rename(GridFSDBFile file, String newName){
        ObjectId id = (ObjectId)file.getId();
        DBObject query = new BasicDBObject(Operator.ID, id);
        DBObject dbo = files.findOne(query);
        dbo.put(FILENAME, newName);
        files.save(dbo);
    }
    
    public void renameFolder(String oldName, String newName){
        DBObject query = new BasicDBObject(FOLDER, oldName);
        DBObject dbo = new BasicDBObject(FOLDER, newName);
        DBObject set = new BasicDBObject(Operator.SET, dbo);
        files.updateMulti(query, set);
    }
    
    public void move(String filename, String folderName){
        DBObject query = new BasicDBObject(FILENAME, filename);
        DBObject dbo = files.findOne(query);
        dbo.put(FOLDER, folderName);
        files.save(dbo);
    }
    
    public void move(GridFSDBFile file, String folderName){
        ObjectId id = (ObjectId)file.getId();
        DBObject query = new BasicDBObject(Operator.ID, id);
        DBObject dbo = files.findOne(query);
        dbo.put(FOLDER, folderName);
        files.save(dbo);
    }
    
    public void remove(String filename){
        fs.remove(filename);
    }
    
    public void remove(DBObject query){
        fs.remove(query);
    }
    
    public void removeFolder(String folderName){
        DBObject query = new BasicDBObject(FOLDER, folderName);
        fs.remove(query);
    }
    
    private List<GridFSDBFile> toFileList(DBCursor cursor){
        List<GridFSDBFile> list = new ArrayList<GridFSDBFile>();
        while(cursor.hasNext()){
            DBObject dbo = cursor.next();
            ObjectId id = (ObjectId)dbo.get(Operator.ID);
            DBObject query = new BasicDBObject(Operator.ID, id);
            GridFSDBFile f = this.findOne(query);
            list.add(f);
        }
        cursor.close();
        return list;
    }
    public GridFS getFS(){
        return fs;
    }
    public static void main(String[] args) {
    	ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:**/applicationcontext*.xml");
    	
    	GridxFS fs=(GridxFS)context.getBean("GridxFS");
    	File file = new File("c:\\6302790910_c4eb865892_o.jpg");
    	GridFSInputFile fsf = fs.save(file);
    	String filename = fsf.getFilename();
//    	GridFSDBFile a = fs.findOne("6302790910_c4eb865892_o.jpg");
    	System.out.println(filename);
//    	System.out.println(a.get("originalName"));
//    	fs.remove("abceee.jpg");
	}
}
