package com.sumavision.tetris.mims.app.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;
import com.sumavision.tetris.mims.app.material.MaterialFilePO;

/**
 * 存储相关工具（主查询以及数据转换）<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月23日 下午3:51:18
 */
@Component
public class StoreQuery {

	/**
	 * 根据素材（多个）生成预删除存储文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:56:32
	 * @param Collection<MaterialFilePO> materials 素材列表
	 * @return List<PreRemoveStoreFilePO> 预删除存储文件列表
	 */
	public List<PreRemoveFilePO> preRemove(Collection<MaterialFilePO> materials) throws Exception{
		if(materials==null || materials.size()<=0) return null;
		List<PreRemoveFilePO> preRemoveFiles = new ArrayList<PreRemoveFilePO>();
		for(MaterialFilePO material:materials){
			preRemoveFiles.add(preRemove(material));
		}
		return preRemoveFiles;
	}
	
	/**
	 * 根据素材（单个）生成预删除存储文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:53:08
	 * @param MaterialFilePO material 素材文件
	 * @return PreRemoveStoreFilePO 预删除存储文件
	 */
	public PreRemoveFilePO preRemove(MaterialFilePO material) throws Exception{
		PreRemoveFilePO preRemoveFile = new PreRemoveFilePO();
		return preRemoveFile;
	}
	
	/**
	 * 将预删除存储文件押入队列<br/>
	 * <p>
	 * 	这里要启动线程去做，线程要延迟5秒（可变）再加入队列，加入前要<br/>
	 *  检验预删除文件数据是否存在，如果不存在认为程序发生异常回滚了<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午4:20:49
	 * @throws Exception
	 */
	public void pushPreRemoveFileToQueue(Collection<PreRemoveFilePO> preRemoveFiles) throws Exception{
		//TODO 这里还没有实现
	}
	
}