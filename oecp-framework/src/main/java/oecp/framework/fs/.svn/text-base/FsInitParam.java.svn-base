/**
 * fs - FsInitParam.java
 * copyright 2009-2012 www.doconline.cn
 * 创建人:wangliang	创建时间:2012-6-29上午9:19:51		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */
package oecp.framework.fs;

import java.util.List;

/**
 * Server 初始化参数
 * @author wangliang
 * @date 2012-6-29
 */
public class FsInitParam {
	
	//是否使用本地缓存
	private Boolean useLocalCache;
	//是否使用Memcached缓存
	private Boolean useMemcached;
	//缓存文件类型前缀，可多个，用逗号间隔(类型参照StorageService内的getContentType方法)
	private List<String> cacheTypes;
	//支持的分辨率大小
	private List<String> dpis;
	public Boolean getUseLocalCache() {
		return useLocalCache;
	}
	public void setUseLocalCache(Boolean useLocalCache) {
		this.useLocalCache = useLocalCache;
	}
	public Boolean getUseMemcached() {
		return useMemcached;
	}
	public void setUseMemcached(Boolean useMemcached) {
		this.useMemcached = useMemcached;
	}
	public List<String> getCacheTypes() {
		return cacheTypes;
	}
	public void setCacheTypes(List<String> cacheTypes) {
		this.cacheTypes = cacheTypes;
	}
	public List<String> getDpis() {
		return dpis;
	}
	public void setDpis(List<String> dpis) {
		this.dpis = dpis;
	}
}
