package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.exceptions.NoEnoughEntryTilesException;
import it.polimi.ingsw.packets.ActivateCharacter3Packet;
import it.polimi.ingsw.packets.ActivateCharacterPacket;


public class ClientCharacter5 extends ClientCharacter {
    private int noEntryTiles;
    @Override
    public ActivateCharacterPacket createPacket(View view) {

        if (noEntryTiles > 0) {
            //view.printMessage(getDescription());
            view.printMessage("Select the island you want to move the noEntry Tile to");
            int islandNumber = view.askIslandNumber();

            ActivateCharacter3Packet packet = new ActivateCharacter3Packet(islandNumber - 1);

            noEntryTiles--;
            return packet;
        }
        else
            throw new NoEnoughEntryTilesException("You have exhausted all noEntry tiles on this character card");
    }

    public int getNoEntryTiles() {
        return noEntryTiles;
    }

    public void setNoEntryTiles(int noEntryTiles) {
        this.noEntryTiles = noEntryTiles;
    }
}
