package hu.bme.aut.amorg.examples.nfctest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GameView extends View {

	public interface GameStateListener {
		public void playerMoved(String aFieldState);

		public void gameEnded();
	}

	private GameStateListener gameStateListener;

	private Drawable mDrawableBg;
	private int touchX;
	private int touchY;

	private Paint bluePaint;
	private Paint blackPaint;
	private Paint whitePaint;
	private Paint redPaint;

	private int[][] fields = new int[3][3];
	private int currentPlayer = 1;
	private boolean moved = false;
	private boolean init = false;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		requestFocus();

		mDrawableBg = getResources().getDrawable(R.drawable.logo);
		setBackgroundDrawable(mDrawableBg);

		bluePaint = new Paint();
		bluePaint.setColor(0xFF0022BB);
		bluePaint.setStrokeWidth(5);
		bluePaint.setStyle(Style.STROKE);

		blackPaint = new Paint();
		blackPaint.setColor(0xBB000000);
		blackPaint.setStyle(Style.FILL);

		whitePaint = new Paint();
		whitePaint.setColor(0xFFFFFFFF);
		whitePaint.setStrokeWidth(5);
		whitePaint.setStyle(Style.STROKE);

		redPaint = new Paint();
		redPaint.setColor(0xFFFF0000);
		redPaint.setStrokeWidth(5);
		redPaint.setStyle(Style.STROKE);
	}
	
	public void setGameStateListener(GameStateListener aListener) {
		gameStateListener = aListener;
	}

	public void initField(String aFieldData) {
		init = true;
		String[] state = aFieldData.split("#");
		currentPlayer = Integer.parseInt(state[0]);

		String[] rawFields = state[1].split(";");
		for (int i = 0; i < rawFields.length; i++) {
			String[] row = rawFields[i].split(",");
			for (int j = 0; j < row.length; j++) {
				if (row[j].equals("1"))
					fields[j][i] = 1;
				else if (row[j].equals("2"))
					fields[j][i] = 2;
				else
					fields[j][i] = 0;
			}
		}

		invalidate();
		
		if (currentPlayer == 1)
			Toast.makeText(getContext(), "You are with: O", Toast.LENGTH_LONG).show();
		else
			Toast.makeText(getContext(), "You are with: X", Toast.LENGTH_LONG).show();
	}

	public String getFieldString() {
		String result = "";
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				result += fields[j][i];
				if (j != 2)
					result += ",";
			}
			if (i != 2)
				result += ";";
		}
		return currentPlayer + "#" + result;
	}

	// returns the winner: O=1; X=2; field is not full=0; field is full=-1
	private int getWinner() {
		boolean row1_O = fields[0][0] == 1 && fields[1][0] == 1
				&& fields[2][0] == 1;
		boolean row2_O = fields[0][1] == 1 && fields[1][1] == 1
				&& fields[2][1] == 1;
		boolean row3_O = fields[0][2] == 1 && fields[1][2] == 1
				&& fields[2][2] == 1;
		boolean col1_O = fields[0][0] == 1 && fields[0][1] == 1
				&& fields[0][2] == 1;
		boolean col2_O = fields[1][0] == 1 && fields[1][1] == 1
				&& fields[1][2] == 1;
		boolean col3_O = fields[2][0] == 1 && fields[2][1] == 1
				&& fields[2][2] == 1;
		boolean diag1_O = fields[0][0] == 1 && fields[1][1] == 1
				&& fields[2][2] == 1;
		boolean diag2_O = fields[2][0] == 1 && fields[1][1] == 1
				&& fields[0][2] == 1;

		boolean row1_X = fields[0][0] == 2 && fields[1][0] == 2
				&& fields[2][0] == 2;
		boolean row2_X = fields[0][1] == 2 && fields[1][1] == 2
				&& fields[2][1] == 2;
		boolean row3_X = fields[0][2] == 2 && fields[1][2] == 2
				&& fields[2][2] == 2;
		boolean col1_X = fields[0][0] == 2 && fields[0][1] == 2
				&& fields[0][2] == 2;
		boolean col2_X = fields[1][0] == 2 && fields[1][1] == 2
				&& fields[1][2] == 2;
		boolean col3_X = fields[2][0] == 2 && fields[2][1] == 2
				&& fields[2][2] == 2;
		boolean diag1_X = fields[0][0] == 2 && fields[1][1] == 2
				&& fields[2][2] == 2;
		boolean diag2_X = fields[2][0] == 2 && fields[1][1] == 2
				&& fields[0][2] == 2;

		if (row1_O || row2_O || row3_O || col1_O || col2_O || col3_O || diag1_O
				|| diag2_O)
			return 1;
		else if (row1_X || row2_X || row3_X || col1_X || col2_X || col3_X
				|| diag1_X || diag2_X)
			return 2;
		else // check if draw
		{
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (fields[i][j] == 0)
						return 0;
				}
			}

			return -1;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawRect(0, 0, getWidth(), getHeight(), blackPaint);

		canvas.drawLine(0, 0, 0, getHeight(), whitePaint);
		canvas.drawLine(0, 0, getWidth(), 0, whitePaint);
		canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), whitePaint);
		canvas.drawLine(0, getHeight(), getWidth(), getHeight(), whitePaint);

		canvas.drawLine(getWidth() / 3, 0, getWidth() / 3, getHeight(),
				whitePaint);
		canvas.drawLine(2 * getWidth() / 3, 0, 2 * getWidth() / 3, getHeight(),
				whitePaint);
		canvas.drawLine(0, getHeight() / 3, getWidth(), getHeight() / 3,
				whitePaint);
		canvas.drawLine(0, 2 * getHeight() / 3, getWidth(),
				2 * getHeight() / 3, whitePaint);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (fields[i][j] == 1)
					canvas.drawCircle((i * 2 + 1) * getWidth() / 6, (j * 2 + 1)
							* getHeight() / 6, getWidth() / 6 - 20, whitePaint);
				else if (fields[i][j] == 2) {
					canvas.drawLine(i * getWidth() / 3 + 20, j * getHeight()
							/ 3 + 20, (i + 1) * getWidth() / 3 - 20, (j + 1)
							* getHeight() / 3 - 20, whitePaint);
					canvas.drawLine((i + 1) * getWidth() / 3 - 20, j
							* getHeight() / 3 + 20, i * getWidth() / 3 + 20,
							(j + 1) * getHeight() / 3 - 20, whitePaint);
				}
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int w = MeasureSpec.getSize(widthMeasureSpec);
		int h = MeasureSpec.getSize(heightMeasureSpec);
		int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
		setMeasuredDimension(d, d);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!moved && init) {
			int action = event.getAction();

			if (action == MotionEvent.ACTION_DOWN) {
				return true;

			} else if (action == MotionEvent.ACTION_UP) {
				touchX = (int) event.getX();
				touchY = (int) event.getY();

				int x = touchX / (getWidth() / 3);
				int y = touchY / (getHeight() / 3);
				if (x < 3 && y < 3) {
					fields[x][y] = currentPlayer;
					invalidate();
					moved = true;

					int winner = getWinner();
					if (winner == 1) {
						Toast.makeText(getContext(), "Winner is: O",
								Toast.LENGTH_LONG).show();
						gameStateListener.gameEnded();
					} else if (winner == 2) {
						Toast.makeText(getContext(), "Winner is: X",
								Toast.LENGTH_LONG).show();
						gameStateListener.gameEnded();
					} else if (winner == -1) {
						Toast.makeText(getContext(), "Draw!", Toast.LENGTH_LONG)
								.show();
						gameStateListener.gameEnded();
					} else {
						currentPlayer = (currentPlayer == 1) ? 2 : 1;
						gameStateListener.playerMoved(getFieldString());
						Toast.makeText(getContext(), "Thank you for your move, please save it via NFC!",
							Toast.LENGTH_LONG).show();
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
}
