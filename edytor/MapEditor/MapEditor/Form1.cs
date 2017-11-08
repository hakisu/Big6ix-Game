using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.IO;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace MapEditor
{

    public partial class Form1 : Form
    {
        public int tileSize;
        public int height;
        public int width;
        Graphics g,k;
        public Image obrazek, podglad1;
        public string[,] tablicaObrazkow;
        public int x, y;
        StreamWriter stream;
        //public string sciezka;
        public Form1()
        {
            tileSize = 64;
            InitializeComponent();
           
            height = (int)wysokosc.Value;
            width = (int)szerokosc.Value;
            pictureBox1.Image = new Bitmap(pictureBox1.Height, pictureBox1.Width);
            podglad.Image = new Bitmap(64, 64);
            k = Graphics.FromImage(podglad.Image);
            g = Graphics.FromImage(pictureBox1.Image);
            g.Clear(Color.White);
            //sciezka = @"C:\Users\Mój komputer\Desktop\edytor\MapEditor\MapEditor\1.jpeg";
            //obrazek = Image.FromFile(sciezka);

            siatka();
           
        }

        private void wysokosc_ValueChanged(object sender, EventArgs e)
        {
            height = (int)wysokosc.Value;
            siatka();
        }

        private void szerokosc_ValueChanged(object sender, EventArgs e)
        {
            width = (int)szerokosc.Value;
           
            siatka();
        }

        private void siatka()
        {
            int odstep = 0;
            g.Clear(Color.White);
            for (int i=0; i<=width;i++)
            {
                g.DrawLine(new Pen(Color.Black), odstep, 0, odstep, tileSize * height);
                odstep += tileSize;
            }
            odstep = 0;
            for (int i = 0; i <= height; i++)
            {
                g.DrawLine(new Pen(Color.Black), 0, odstep, tileSize*width, odstep);
                odstep += tileSize;
            }
            pictureBox1.Refresh();

        }

        private void pictureBox1_MouseClick(object sender, MouseEventArgs e)
        {
            
            x = e.X / tileSize;
            y = e.Y / tileSize;
            if (x < width && y < height && podglad1 != null)
            {
                if (wysokosc.Enabled == true)
                {
                    tablicaObrazkow = new string[height, width];
                    wysokosc.Enabled = false;
                    szerokosc.Enabled = false;
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            tablicaObrazkow[i, j] = "0";
                        }
                    }
                }
                g.DrawImage(podglad1, x * tileSize, y * tileSize);
                tablicaObrazkow[y,x] = Path.GetFileNameWithoutExtension(openFileDialog1.FileName);
                pictureBox1.Refresh();
            }
            
        }

        private void button1_Click(object sender, EventArgs e)
        {
            openFileDialog1.ShowDialog();
            podglad1 = Image.FromFile(openFileDialog1.FileName);
            k.DrawImage(podglad1, 0, 0);
            podglad.Refresh();
        
        }

        private void zapisz_btn_Click(object sender, EventArgs e)
        {
            stream = new StreamWriter(textBox1.Text);
            for (int i = 0; i < height; i++)
            {
                for (int j = 0; j < width; j++)
                {
                    stream.Write(tablicaObrazkow[i, j]+",");
                }
                stream.Write("\r\n");

            }
            stream.Flush();
            stream.Close();
        }
    }
}
