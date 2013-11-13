package com.javagameengine.util;

import java.util.Arrays;

/**
 * Describes an array-based buffer with a limited capacity. Writing to the buffer will push out the oldest
 * element if the buffer is full. Random-access and iteration are allowed. 
 * <p>
 * The buffer provides methods to iterate through it in either direction by maintaining a separate index for 
 * reading and writing to the internal array. Either indices can be retrieved and set to manipulate the buffer.
 * The methods read, readBackwards, and readForwards retrieve the elements in the buffer using the read index. 
 * <p>
 * In addition to the iterative read methods, there is also get, getHead, and getTail which do not modify the
 * read index.
 * 
 * @author ClairaLyrae
 * @param <T> Object type to store in buffer
 */
public class CyclicArrayBuffer<T>
{ 
	private T[] buffer;
	private int readPos;
	private int writePos;
	
	/**
	 * Create a new CyclicArrayBuffer with an internal array of the given size
	 * @param size Size of buffer
	 */
	public CyclicArrayBuffer(int size)
	{
		buffer = (T[])new Object[size];
	}
	
	/**
	 * @return Element at current read index
	 */
	public T read()
	{
		return buffer[readPos];
	}
	
	/**
	 * Resize the buffer to the given size.
	 * @param s Size of buffer
	 */
	public void resize(int s)
	{
		buffer = Arrays.copyOf(buffer, s);
		if(writePos > buffer.length)
			writePos = buffer.length - 1;
		if(readPos > buffer.length)
			readPos = buffer.length - 1;
	}

	/**
	 * Returns the element at the current read index, then increments the read index
	 * @return Element at current read index
	 */
	public T readForwards()
	{
		if(readPos == writePos)
			return null;
		readPos ++;
		if(readPos >= buffer.length)
			readPos = 0;
		return buffer[readPos];
	}

	/**
	 * Returns the element at the current read index, then decrements the read index
	 * @return Element at current read index
	 */
	public T readBackwards()
	{
		readPos--;
		if(readPos < 0)
			readPos += buffer.length;
		if(writePos == readPos)
		{
			readPos = writePos + 1;
			return null;
		}
		return buffer[readPos];
	}
	
	/**
	 * Writes the given element to the buffer at the write index, and increments the index
	 * @param o Element to write to buffer
	 */
	public void write(T o)
	{
		if(++writePos >= buffer.length)
			writePos = 0;
		if(++readPos >= buffer.length)
			readPos = 0;
		buffer[writePos] = o;
	}
	
	/**
	 * Sets the read index to the given position. Throws an IllegalStateException if the
	 * index is invalid.
	 * @param i New position of read index
	 */
	public void setReadPos(int i)
	{
		if(i < 0 || i >= buffer.length)
			throw new IllegalStateException();
		readPos = i;
	}

	/**
	 * Sets the write index to the given position. Throws an IllegalStateException if the
	 * index is invalid.
	 * @param i New position of write index
	 */
	public void setWritePos(int i)
	{
		if(i < 0 || i >= buffer.length)
			throw new IllegalStateException();
		writePos = i;
	}
	
	public int getReadPos()
	{
		return readPos;
	}
	
	public int getWritePos()
	{
		return writePos;
	}
	
	/**
	 * Sets the read index to the head of the buffer (newest element).
	 */
	public void setReadToHead()
	{
		readPos = writePos;
	}
	
	/**
	 * Sets the read index to the tail of the buffer (oldest element)
	 */
	public void setReadToTail()
	{
		readPos = writePos + 1;
		if(readPos >= buffer.length)
			readPos = 0;
	}
	
	/**
	 * Retrieves the oldest element in the buffer. Does not affect the read position.
	 * @return Oldest element in buffer
	 */
	public T getTail()
	{
		if(buffer.length == 0)
			return null;
		int readtemp = writePos + 1;
		if(readtemp >= buffer.length)
			readtemp = 0;
		return buffer[readtemp];
	}

	/**
	 * Retrieves the newest element in the buffer. Does not affect the read position.
	 * @return Newest element in buffer
	 */
	public T getHead()
	{
		if(buffer.length == 0)
			return null;
		return buffer[writePos];
	}
	
	public int size()
	{
		return buffer.length;
	}

	/**
	 * Retrieves the element in the given position in the buffer. The position is referenced
	 * from the head, where 0 is the newest element and size()-1 is the oldest element. Does not
	 * affect the read index. Throws an IllegalStateException if the given element would specify
	 * an element older than the oldest element the buffer.
	 * @return Newest element in buffer
	 */
	public T get(int age)
	{
		if(age < 0 || age >= buffer.length)
			throw new IllegalStateException();
		if(buffer.length == 0)
			return null;
		int readtemp = readPos;
		setReadToHead();
		while(age-- > 0)
			readBackwards();
		T element = buffer[readPos];
		readPos = readtemp;
		return element;
	}
}
