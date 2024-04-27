using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace parsingProject
{
    class Program
    {
        
        static void Main()
        {
            // CP949와 같은 추가 인코딩을 사용 가능하도록 등록
            Encoding.RegisterProvider(CodePagesEncodingProvider.Instance);


            string sourceFilePath = @"C:\Users\gepodong\Documents\000_엘리스트랙\000-santa\DataParsingTest\mountainsOriginData.csv"; // CSV 파일 경로
            string destinationFilePath = @"C:\Users\gepodong\Documents\000_엘리스트랙\000-santa\Result\parsingResult.txt"; // 결과를 저장할 파일 경로

            try
            {
                using (StreamReader reader = new StreamReader(sourceFilePath, Encoding.Default))
                using (StreamWriter writer = new StreamWriter(destinationFilePath, false, Encoding.Default))
                {
                    while (!reader.EndOfStream)
                    {
                        var line = reader.ReadLine();
                        var values = line.Split(','); 

                        // 한줄데이터를 (,)컴마 기준으로 나누어서 순회한다.
                        var resultString = "(";
                        for (int i = 0; i < values.Length; i++)
                        {
                            var getString = values[i];
                            
                            if (0 == i)
                            {
                                // i 번째 단어에만 특수한 처리를 하고싶다면 i 인덱스를 가지고 처리해주면된다. 
                            } 

                            resultString += "'" + getString + "',"; // DB 컬럼 값 타입이 string이라서 작은따옴표로 묶어준다.                            
                        }

                        resultString += "),";  //  다음행의 데이터와 구별하기 위해 , 추가한다. 
                        writer.WriteLine(resultString);
                    }
                }

                Console.WriteLine("파일 파싱 및 저장이 완료되었습니다.");
            }
            catch (Exception ex)
            {
                Console.WriteLine("오류 발생: " + ex.Message);
            }
        }
    }


}
