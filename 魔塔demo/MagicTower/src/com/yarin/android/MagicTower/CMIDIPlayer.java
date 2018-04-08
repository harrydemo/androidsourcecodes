package com.yarin.android.MagicTower;

import java.io.IOException;

import android.media.MediaPlayer;

public class CMIDIPlayer
{
	public MediaPlayer	playerMusic;

	public MagicTower	magicTower	= null;


	public CMIDIPlayer(MagicTower magicTower)
	{
		this.magicTower = magicTower;

	}


	// ��������
	public void PlayMusic(int ID)
	{
		FreeMusic();
		switch (ID)
		{
			case 1:
				//װ������
				playerMusic = MediaPlayer.create(magicTower, R.raw.menu);
				//����ѭ��
				playerMusic.setLooping(true);
				try
				{
					//׼��
					playerMusic.prepare();
				}
				catch (IllegalStateException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				//��ʼ
				playerMusic.start();
				break;
			case 2:
				playerMusic = MediaPlayer.create(magicTower, R.raw.run);
				playerMusic.setLooping(true);
				try
				{
					playerMusic.prepare();
				}
				catch (IllegalStateException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				playerMusic.start();
				break;
		}
	}


	// �˳��ͷ���Դ
	public void FreeMusic()
	{
		if (playerMusic != null)
		{
			playerMusic.stop();
			playerMusic.release();
		}
	}


	// ֹͣ����
	public void StopMusic()
	{
		if (playerMusic != null)
		{
			playerMusic.stop();
		}
	}
}
