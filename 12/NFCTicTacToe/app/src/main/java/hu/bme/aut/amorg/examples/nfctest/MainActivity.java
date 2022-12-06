package hu.bme.aut.amorg.examples.nfctest;

import hu.bme.aut.amorg.examples.nfctest.GameView.GameStateListener;

import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements GameStateListener {
	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;
	private IntentFilter[] mWriteTagFilters;
	private boolean inWriteMode = false;

	private void enableTagWriteMode() {
		inWriteMode = true;
		IntentFilter tagDetected = new IntentFilter(
				NfcAdapter.ACTION_TAG_DISCOVERED);
		mWriteTagFilters = new IntentFilter[] { tagDetected };
		mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent,
				mWriteTagFilters, null);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// Tag writing mode
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

			if (!fieldStateToSave.equals("")) {
				NdefRecord record = createCustomRecord(fieldStateToSave);

				NdefMessage msg = new NdefMessage(new NdefRecord[] { record });

				if (writeTag(msg, detectedTag)) {
					Toast.makeText(this, "Success: game state saved!",
							Toast.LENGTH_LONG).show();
					finish();
				} else {
					Toast.makeText(this, "Write failed", Toast.LENGTH_LONG)
							.show();
				}
			}
		}
	}

	public NdefRecord createCustomRecord(String aData) {
		byte[] payload = aData.getBytes();
		NdefRecord extRecord = new NdefRecord(NdefRecord.TNF_EXTERNAL_TYPE,
				"com.example:externalType".getBytes(), new byte[0], payload);
		return extRecord;
	}

	public static boolean writeTag(NdefMessage message, Tag tag) {
		int size = message.toByteArray().length;
		try {
			Ndef ndef = Ndef.get(tag);
			if (ndef != null) {
				ndef.connect();
				if (!ndef.isWritable()) {
					return false;
				}
				if (ndef.getMaxSize() < size) {
					return false;
				}
				ndef.writeNdefMessage(message);
				return true;
			} else {
				NdefFormatable format = NdefFormatable.get(tag);
				if (format != null) {
					try {
						format.connect();
						format.format(message);
						return true;
					} catch (IOException e) {
						return false;
					}
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
	}

	private GameView gameView;
	private String fieldStateToSave = "1#0,0,0;0,0,0;0,0,0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		// Intent filters for exchanging over p2p.
		IntentFilter ndefDetected = new IntentFilter(
				NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndefDetected.addDataType("text/plain");
		} catch (MalformedMimeTypeException e) {
			
		}

		gameView = (GameView) findViewById(R.id.game_view);
		gameView.setGameStateListener(this);
	}

	public void onResume() {
		super.onResume();
		if (!inWriteMode) {
			if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent()
					.getAction())) {
				NdefMessage[] msgs = null;

				Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(
						NfcAdapter.EXTRA_NDEF_MESSAGES);
				if (rawMsgs != null) {
					msgs = new NdefMessage[rawMsgs.length];
					for (int i = 0; i < rawMsgs.length; i++) {
						msgs[i] = (NdefMessage) rawMsgs[i];
					}
				}

				if (msgs != null) {
					for (NdefMessage tmpMsg : msgs) {
						gameView.initField(new String(tmpMsg.getRecords()[0]
								.getPayload()));
					}
				}
			}
		}
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_clear_game) {
			gameEnded();
			Toast.makeText(this, "Please put the device near to NFC tag to clear the board!", Toast.LENGTH_LONG).show();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onClick(View v) {
		enableTagWriteMode();
	}

	public void playerMoved(String aFieldState) {
		fieldStateToSave = aFieldState;
		enableTagWriteMode();
	}

	public void gameEnded() {
		fieldStateToSave = "1#0,0,0;0,0,0;0,0,0";
		enableTagWriteMode();
	}
}
