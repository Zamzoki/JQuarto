package quarto.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JPanel;

import quarto.engine.StateManager;
import quarto.engine.board.Board;
import quarto.engine.pieces.Piece;

@SuppressWarnings("serial")
public class PiecesPanel extends JPanel
{
	public final static int PIECES_PER_ROW = 8;
	public final static int PIECES_PER_COLUMN = 2;
	public final static int PIECE_SIZE = GameWindow.WINDOW_WIDTH / PIECES_PER_ROW;	
	public final static int PIECES_PANEL_WIDTH = GameWindow.WINDOW_WIDTH;
	public final static int PIECES_PANEL_HEIGHT = PIECES_PER_ROW * PIECE_SIZE;
	
	private boolean areMouseListenersEnabled = true;
	private Board board;
	private Map<JLabel, Piece> pieceLabelPieceMap;
	private Map<JLabel, MouseListener> pieceLabelMouseListenerMap;
	
	public PiecesPanel(Board board) 
	{
		this.board = board;
		this.pieceLabelPieceMap = new HashMap<>();
		this.pieceLabelMouseListenerMap = new HashMap<>();
		
		this.configure();
	}
	
	private void configure()
	{	
		this.setLayout(new GridLayout(PIECES_PER_COLUMN, PIECES_PER_ROW));
		this.setSize(new Dimension(PIECES_PANEL_WIDTH, PIECES_PANEL_HEIGHT));
	}
	
	public Board getBoard()
	{
		return this.board;
	}
	
	public void setBoard(Board board)
	{
		this.board = board;
	}
	
	public void drawPieces()
	{
		this.removeAll();
		
		List<Piece> remainingPieces = this.board.getRemainingPieces();
		
		for(int i = 0; i < remainingPieces.size(); i++)
		{
			JLabel pieceLabel = new JLabel();
			
			if(remainingPieces.get(i) != null)
			{
				pieceLabel.setIcon(GuiHelper.PIECES_ICONS.get(remainingPieces.get(i).getPieceNumberAsString()));
				pieceLabel.setOpaque(true);
				
				pieceLabelPieceMap.put(pieceLabel, remainingPieces.get(i));
				pieceLabelMouseListenerMap.put(pieceLabel, null);
			}
			
			this.add(pieceLabel);
		}
		
		addMouseListeners();
	}
	
	public void updatePieces()
	{
		Iterator<Entry<JLabel, Piece>> mapIterator = pieceLabelPieceMap.entrySet().iterator();
		
		while(mapIterator.hasNext())
		{
			Entry<JLabel, Piece> mapElement = mapIterator.next();
			Piece piece = mapElement.getValue();
			
			if(!this.board.getRemainingPieces().contains(piece))
			{
				JLabel pieceLabel = mapElement.getKey();
				pieceLabel.setIcon(null);
				pieceLabel.setOpaque(false);
			}
		}
	}
	
	public void disableMouseListeners()
	{
		this.areMouseListenersEnabled = false;
	}
	
	public void enableMouseListeners()
	{
		this.areMouseListenersEnabled = true;
	}
	
	private void addMouseListeners()
	{
		Iterator<Entry<JLabel, MouseListener>> mapIterator = this.pieceLabelMouseListenerMap.entrySet().iterator();
		
		while(mapIterator.hasNext())
		{
			JLabel label = mapIterator.next().getKey();
			
			MouseListener mouseListener = new MouseListener() 
			{
				boolean isMouseListenerEnabled = true;
				
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e) {}
				
				@Override
				public void mouseExited(MouseEvent e) 
				{
					if(	!StateManager.isPieceChosen &&
						isMouseListenerEnabled && areMouseListenersEnabled)
					{
						label.setBackground(null);	
					}
				}
				
				@Override
				public void mouseEntered(MouseEvent e) 
				{
					if(	!StateManager.isPieceChosen &&
						isMouseListenerEnabled && areMouseListenersEnabled)
					{
						label.setBackground(new Color(133, 133, 133));	
					}					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) 
				{
					if(isMouseListenerEnabled && areMouseListenersEnabled)
					{
						label.setBackground(new Color(24, 179, 0));
						pieceLabelPieceMap.get(label).choose();
						
						isMouseListenerEnabled = false;
						
						StateManager.pieceChosen();
					}
				}
			};
			
			label.addMouseListener(mouseListener);
			this.pieceLabelMouseListenerMap.put(label, mouseListener);
		}
	}
}




















