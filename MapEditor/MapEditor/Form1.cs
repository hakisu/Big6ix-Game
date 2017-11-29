using System;
using System.Collections;
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
        Graphics g, k;
        public Image obrazek, podglad1;
        public string[,] tablicaObrazkow;
        public int x, y;
        public List<Point> listaPrzejsc;
        public List<Image> listaObrazkow;
        StreamWriter stream;
        public string outputPath;
        Hashtable hashTable = new Hashtable();
        public string inputPath;
        public string propertiesPath;
        private int initialAmountOfRows = 10;
        private int initialAmountOfColumns = 10;

        public Form1()
        {
            InitializeComponent();
            tileSize = 64;
            outputPath = @"..\..\..\..\core\assets\data\rooms\";
            pictureBox1.Image = new Bitmap(pictureBox1.Height, pictureBox1.Width);
            podglad.Image = new Bitmap(64, 64);
            k = Graphics.FromImage(podglad.Image);
            g = Graphics.FromImage(pictureBox1.Image);
            g.Clear(Color.White);
            wysokosc.Value = initialAmountOfRows;
            szerokosc.Value = initialAmountOfColumns;
            height = (int)wysokosc.Value;
            width = (int)szerokosc.Value;

            inputPath = @"..\..\..\..\core\assets\graphics\map\";
            propertiesPath = inputPath + "tiles.properties";
            String[] fileLines = File.ReadAllLines(propertiesPath);
            foreach (String line in fileLines)
            {
                String[] currentLineTokens = line.Split(' ');
                String currentPictureName = currentLineTokens[0];
                int currentPictureValue = int.Parse(currentLineTokens[1]);
                hashTable.Add(currentPictureName, currentPictureValue);
                comboBox1.Items.Add(currentPictureName);
            }
            Image nowy;
            listaObrazkow = new List<Image>();
            foreach (String currentPictureName in comboBox1.Items)
            {
                nowy = Image.FromFile(inputPath + currentPictureName);
                listaObrazkow.Add(nowy);
            }

            comboBox1.SelectedIndex = 0;
            siatka();
        }

        private void wysokosc_ValueChanged(object sender, EventArgs e)
        {
            height = (int)wysokosc.Value;
            if (height * tileSize >= pictureBox1.Height)
            {
                pictureBox1.Height = tileSize * height + 20;
                pictureBox1.Image = new Bitmap(pictureBox1.Width, pictureBox1.Height);
            }
            g = Graphics.FromImage(pictureBox1.Image);
            siatka();
        }

        private void szerokosc_ValueChanged(object sender, EventArgs e)
        {
            width = (int)szerokosc.Value;
            if (width * tileSize >= pictureBox1.Width)
            {
                pictureBox1.Width = tileSize * width + 20;
                pictureBox1.Image = new Bitmap(pictureBox1.Width, pictureBox1.Height);
            }
            g = Graphics.FromImage(pictureBox1.Image);
            siatka();
        }

        private void siatka()
        {
            int odstep = 0;
            g.Clear(Color.White);
            for (int i = 0; i <= width; i++)
            {
                g.DrawLine(new Pen(Color.Black), odstep, 0, odstep, tileSize * height);
                odstep += tileSize;
            }
            odstep = 0;
            for (int i = 0; i <= height; i++)
            {
                g.DrawLine(new Pen(Color.Black), 0, odstep, tileSize * width, odstep);
                odstep += tileSize;
            }
            pictureBox1.Refresh();

        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            podglad1 = Image.FromFile(inputPath + comboBox1.SelectedItem);
            Rectangle rect = new Rectangle(0, 0, 64, 64);
            k.DrawImage(podglad1, rect);
            podglad.Refresh();
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
                    listaPrzejsc = new List<Point>();
                    wysokosc.Enabled = false;
                    szerokosc.Enabled = false;
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {
                            tablicaObrazkow[i, j] = "-1";
                        }
                    }
                }

                Rectangle rect = new Rectangle(x * tileSize, y * tileSize, 64, 64);
                g.DrawImage(podglad1, rect);
                foreach (DictionaryEntry entry in hashTable)
                {
                    if (entry.Key == comboBox1.SelectedItem)
                        tablicaObrazkow[y, x] = entry.Value.ToString();
                }


                if (checkBox1.Checked)
                {
                    g.DrawEllipse(new Pen(Color.Red, 3), rect.X + 5, rect.Y + 5, 5, 5);
                    listaPrzejsc.Add(new Point(x, y));
                }
                else if (!checkBox1.Checked && listaPrzejsc.Contains(new Point(x, y)))
                {
                    listaPrzejsc.Remove(new Point(x, y));
                }
                pictureBox1.Refresh();


            }

        }

        private void zapisz_btn_Click(object sender, EventArgs e)
        {
            if (tablicaObrazkow == null)
            {
                MessageBox.Show("Nie mozna zapisac pustej planszy!");
            }
            else
            {
                stream = new StreamWriter(outputPath + textBox1.Text + ".room");
                stream.Write(szerokosc.Value.ToString() + " " + wysokosc.Value.ToString() + "\r\n");
                for (int i = 0; i < height; i++)
                {
                    for (int j = 0; j < width; j++)
                    {
                        stream.Write(tablicaObrazkow[i, j] + " ");
                    }
                    stream.Write("\r\n");

                }
                foreach (Point p in listaPrzejsc)
                {
                    stream.Write(p.X.ToString() + " " + p.Y.ToString() + "\r\n");
                }
                stream.Flush();
                stream.Close();
            }
        }

        protected override void OnMouseWheel(MouseEventArgs e)
        {
            int currentIndex = comboBox1.SelectedIndex;
            if (e.Delta < 0)
            {
                currentIndex++;
                if (currentIndex >= comboBox1.Items.Count)
                {
                    currentIndex = 0;
                }
                comboBox1.SelectedIndex = currentIndex;
            }
            if (e.Delta > 0)
            {
                currentIndex--;
                if (currentIndex <= -1)
                {
                    currentIndex = comboBox1.Items.Count - 1;
                }
                comboBox1.SelectedIndex = currentIndex;
            }
        }
    }
}