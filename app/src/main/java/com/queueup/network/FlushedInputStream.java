/*    */ package com.queueup.network;
/*    */ 
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class FlushedInputStream extends FilterInputStream
/*    */ {
/*    */   public FlushedInputStream(InputStream inputStream)
/*    */   {
/* 11 */     super(inputStream);
/*    */   }
/*    */ 
/*    */   public long skip(long n)
/*    */     throws IOException
/*    */   {
/* 17 */     long totalBytesSkipped = 0L;
/* 18 */     while (totalBytesSkipped < n)
/*    */     {
/* 20 */       long bytesSkipped = this.in.skip(n - totalBytesSkipped);
/* 21 */       if (bytesSkipped == 0L)
/*    */       {
/* 23 */         int b = read();
/* 24 */         if (b < 0)
/*    */         {
/*    */           break;
/*    */         }
/* 28 */         bytesSkipped = 1L;
/*    */       }
/*    */ 
/* 31 */       totalBytesSkipped += bytesSkipped;
/*    */     }
/* 33 */     return totalBytesSkipped;
/*    */   }
/*    */ }

/* Location:           E:\new\temp\equalcomponent-Release1.0.jar
 * Qualified Name:     com.equal.logicomponent.java.io.FlushedInputStream
 * JD-Core Version:    0.6.2
 */