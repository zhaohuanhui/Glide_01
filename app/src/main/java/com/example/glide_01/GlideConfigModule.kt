package com.example.glide_01

import android.content.Context
import android.content.ContextWrapper
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import java.io.File


@GlideModule
class GlideConfigModule : AppGlideModule() {

    //磁盘缓存外部存储大小
    private val diskCacheSize = 250 * 1024 * 1024L

    //缓存大小
    private val memoryCacheSize = 250 * 1024 * 1024L

    //Bitmap池大小
    private val bitmapPoolSize = 250 * 1024 * 1024L

    //磁盘缓存外部存储的绝对路径
    //getExternalStorageDirectory 已过时  getExternalFilesDir替代
    //var path: String = Environment.getExternalStorageDirectory().path.toString() + "/GlideImage"
    //磁盘缓存外部存储指定的目录
    //private var diskCacheFolder = File(path)

    //磁盘缓存外部存储的绝对路径
    private var path: String = "/ImageCache/GlideImage"
    private var mContextWrapper = ContextWrapper(App.instance)

    //磁盘缓存外部存储指定的目录
    private var diskCacheFolder: File = mContextWrapper.getExternalFilesDir(path)!!

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        //修改默认配置，如缓存配置
        //磁盘缓存配置（默认缓存大小250M，默认保存在内部存储中）
        //设置磁盘缓存保存在外部存储，且指定缓存大小
        builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context, diskCacheSize))

        //设置磁盘缓存保存在自己指定的目录下，且指定缓存大小
        builder.setDiskCache(
            DiskLruCacheFactory(
                DiskLruCacheFactory.CacheDirectoryGetter { diskCacheFolder },
                diskCacheSize
            )
        )

        //内存缓存配置（不建议配置，Glide会自动根据手机配置进行分配）
        //设置内存缓存大小
        builder.setMemoryCache(LruResourceCache(memoryCacheSize))

        //设置Bitmap池大小
        builder.setBitmapPool(LruBitmapPool(bitmapPoolSize));
        /**
         * DiskCacheStrategy.NONE： 表示不缓存任何内容。
         * DiskCacheStrategy.DATA： 表示只缓存原始图片。
         * DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
         * DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
         * DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
         */
        builder.setDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        //替换组件，如网络请求组件

    }
}