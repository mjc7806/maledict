/*
 * Maledict - An Interactive Tool for Learning Linear and Differential Cryptanalysis of SPNs
 * Copyright (C) 2016  Mike Carpenter
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.mjcarpenter.maledict.crypto.ldc;

public abstract class AbstractApproximation
{
	private final long plaintextMask;
	private final long lastRoundMask;
	
	public AbstractApproximation(long plaintextMask, long lastRoundMask)
	{
		this.plaintextMask = plaintextMask;
		this.lastRoundMask = lastRoundMask;
	}
	
	public long getPlaintextMask()
	{
		return plaintextMask;
	}
	
	public long getLastRoundMask()
	{
		return lastRoundMask;
	}
}
