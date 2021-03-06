/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2014, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.core.sync.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import net.minecraft.entity.player.EntityPlayer;
import appeng.api.util.AEColor;
import appeng.core.sync.AppEngPacket;
import appeng.core.sync.network.INetworkInfo;
import appeng.hooks.TickHandler;
import appeng.hooks.TickHandler.PlayerColor;

public class PacketPaintedEntity extends AppEngPacket
{

	private final AEColor myColor;
	private final int entityId;
	private int ticks;

	// automatic.
	public PacketPaintedEntity(ByteBuf stream)
	{
		entityId = stream.readInt();
		myColor = AEColor.values()[stream.readByte()];
		ticks = stream.readInt();
	}

	@Override
	public void clientPacketData(INetworkInfo network, AppEngPacket packet, EntityPlayer player)
	{
		PlayerColor pc = new PlayerColor( entityId, myColor, ticks );
		TickHandler.instance.getPlayerColors().put( entityId, pc );
	}

	// api
	public PacketPaintedEntity(int myEntity, AEColor myColor, int ticksLeft) {

		ByteBuf data = Unpooled.buffer();

		data.writeInt( getPacketID() );
		data.writeInt( this.entityId = myEntity );
		data.writeByte( (this.myColor = myColor).ordinal() );
		data.writeInt( ticksLeft );

		configureWrite( data );
	}
}
