using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace MIGestureControl_KinectEmulator
{
  public partial class Form1 : Form
  {
    private int currentJointId;
    private Skeleton skeleton = new Skeleton();

    public Form1()
    {
      InitializeComponent();

      skeleton.Set3DJointPosition(Skeleton.HEAD, 1.0f, 2.0f, 3.0f);
      skeleton.Set3DJointPosition(Skeleton.HAND_RIGHT, 4.0f, 5.0f, 6.0f);
      skeleton.Set3DJointPosition(Skeleton.HAND_LEFT, 7.0f, 8.0f, 9.0f);
      skeleton.Set3DJointPosition(Skeleton.ELBOW_RIGHT, 10.0f, 11.0f, 12.0f);
      skeleton.Set3DJointPosition(Skeleton.ELBOW_LEFT, 13.0f, 14.0f, 15.0f);
      skeleton.Set3DJointPosition(Skeleton.SHOULDER_RIGHT, 16.0f, 17.0f, 18.0f);
      skeleton.Set3DJointPosition(Skeleton.SHOULDER_LEFT, 19.0f, 20.0f, 21.0f);
    }

    private void button1_Click(object sender, EventArgs e)
    {
      TcpClient client = new TcpClient();
      client.Connect(new IPEndPoint(IPAddress.Loopback, 5555));

      BinaryWriter writer = new BinaryWriter(client.GetStream());
      for (int i = 0; i < skeleton.Joints.Length; i++)
        writer.Write(skeleton.Joints[i]);
      writer.Flush();

      client.Close();
    }

    private void buttonJoint_Click(object sender, EventArgs e)
    {
      int jointId = Convert.ToInt32(((Button)sender).Text);
      currentJointId = jointId;

      float x, y, z;
      if(skeleton.Get3DJointPosition(jointId, out x, out y, out z))
      {
        textBox1.Text = x.ToString("F4");
        textBox2.Text = y.ToString("F4");
        textBox3.Text = z.ToString("F4");
      }
    }

    private void button9_Click(object sender, EventArgs e)
    {
      if (currentJointId < 0 || currentJointId >= Skeleton.JOINT_COUNT)
        return;

      float x, y, z;
      x = Convert.ToSingle(textBox1.Text);
      y = Convert.ToSingle(textBox2.Text);
      z = Convert.ToSingle(textBox3.Text);
      skeleton.Set3DJointPosition(currentJointId, x, y, z);
    }
  }

  public class Skeleton
  {
    public const int SPINE_BASE = 0;
    public const int SPINE_MID = 1;
    public const int NECK = 2;
    public const int HEAD = 3;
    public const int SHOULDER_LEFT = 4;
    public const int ELBOW_LEFT = 5;
    public const int WRIST_LEFT = 6;
    public const int HAND_LEFT = 7;
    public const int SHOULDER_RIGHT = 8;
    public const int ELBOW_RIGHT = 9;
    public const int WRIST_RIGHT = 10;
    public const int HAND_RIGHT = 11;
    public const int HIP_LEFT = 12;
    public const int KNEE_LEFT = 13;
    public const int ANKLE_LEFT = 14;
    public const int FOOT_LEFT = 15;
    public const int HIP_RIGHT = 16;
    public const int KNEE_RIGHT = 17;
    public const int ANKLE_RIGHT = 18;
    public const int FOOT_RIGHT = 19;
    public const int SPINE_SHOULDER = 20;
    public const int HAND_TIP_LEFT = 21;
    public const int THUMB_LEFT = 22;
    public const int HAND_TIP_RIGHT = 23;
    public const int THUMB_RIGHT = 24;
    public const int JOINT_COUNT = 25;

    public static string GetJointName(int jointId)
    {
      switch(jointId)
      {
        case 3: return "HEAD";
        case 4: return "SHOULDER_LEFT";
        case 5: return "ELBOW_LEFT";
        case 7: return "HAND_LEFT";
        case 8: return "SHOULDER_RIGHT";
        case 9: return "ELBOW_RIGHT";
        case 11: return "HAND_RIGHT";
        default: return "#UNKNOWN";
      }
  }

    public float[] Joints = new float[JOINT_COUNT * 3];

    public void Set3DJointPosition(int jointId, float x, float y, float z)
    {
      if (jointId < 0 || jointId >= JOINT_COUNT)
        return;

      Joints[jointId * 3] = x;
      Joints[(jointId * 3) + 1] = y;
      Joints[(jointId * 3) + 2] = z;
    }

    public bool Get3DJointPosition(int jointId, out float x, out float y, out float z)
    {
      x = y = z = 0.0f;
      if (jointId < 0 || jointId >= JOINT_COUNT)
        return false;

      x = Joints[jointId * 3];
      y = Joints[(jointId * 3) + 1];
      z = Joints[(jointId * 3) + 2];
      return true;
    }
  }
}
